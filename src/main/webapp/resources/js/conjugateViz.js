/**
 * Created by zhengjiabin on 14-8-3.
 */


function createCommunicateD3Viz(graph, commonMode) {

    var width = 740,
        height = 550;

    var color = d3.scale.category20();

    var force = d3.layout.force()
        .charge(-600)
        .size([width, height]);

    var svg = d3.select("#showBoard").append("svg")
        .attr("width", width)
        .attr("height", height).append("g");

    d3.json("/AMD/resources/json/conjugate.json", function(error, temp) {

        var linksData = commonMode?graph.links:graph.polyLinks;
        force
            .nodes(graph.nodes)
            .links(linksData)
            .linkDistance(function(d) {
                console.log(250 * (1 / d.factor) + 10);
                return 250 * (1 / d.factor) + 10; })
            .start();

        var link = svg.selectAll(".link")
            .data(linksData)
            .enter()
            .append("g")
            .attr("class", "link");

        var line = link.append("line")
            .style("stroke-width", "1px")
            .style("stroke", "grey");

        var linkLabel = link.append("text")
            .attr("class", "linkLabel linkShowTitle")
            .attr("x", function(d) { return (d.source.x + d.target.x) / 2;})
            .attr("y", function(d) { return (d.source.y + d.target.y) / 2;})
            .attr("fill", "rgb(255, 138, 36)")
            .text(function(d) { return d.count;});

        linkLabel.append("title")
            .text(function(d) {
                return "通话记录:" + d.callCount + "    短信记录:" + d.smsCount;
            });

        var circleLine;
        if (commonMode) {
            circleLine = svg.selectAll(".cricleLine")
                .data(graph.circleLinks)
                .enter().append("line")
                .attr("class", "linkShowTitle")
                .style("stroke-width", "2px")
                .style("stroke", "orange")
                .style("stroke-opacity", ".6");

            circleLine.append("title")
                .text(function(d){
                    return "通话记录:" + d.callCount + "    短信记录:" + d.smsCount;
                });
        }

        var node = svg.selectAll(".node")
            .data(graph.nodes)
            .enter().append("g")
            .attr("class", "node")
            .call(force.drag);

        node.append("circle")
            .attr("class", function(d) { return d.recorded?"recorded":"";})
            .attr("r", 10)
            .on("click", function(event) {
                if(shiftDown) {
                    $.ajax({
                        url: '/AMD/conjugate/phoneInfo',
                        type: 'GET',
                        data: 'phoneNum=' + event.name,
                        dataType: 'json',
                        success: function (infoObj) {

                            DetailsTool.removeContent();

                            if (infoObj == null) {
                                DetailsTool.createInfo('该手机号尚未被采集.');
                                DetailsTool.show();
                            } else {
                                // 数据设置
                                DetailsTool.setType('phone');
                                DetailsTool.setSourceNumber(event.name);
                                DetailsTool.setImage('/AMD/resources/img/suspectMan.jpg');
                                DetailsTool.setName(infoObj.oname);
                                DetailsTool.setMobile(infoObj.mobile);
                                DetailsTool.setBirthPlace(infoObj.birthPlace);
                                DetailsTool.setObjectId(infoObj.oid);
                                DetailsTool.setCaseId(infoObj.caseId);
                                DetailsTool.setCertificateType(infoObj.certificateType);
                                DetailsTool.setCertificateId(infoObj.certificateId);
                                DetailsTool.setCollectId(infoObj.collectId);
                                DetailsTool.setPoliceCode(infoObj.policeCode);
                                DetailsTool.setAreaCode(infoObj.areaCode);
                                DetailsTool.setAddress(infoObj.addres);
                                DetailsTool.setTakeTime(infoObj.takeTime);
                                DetailsTool.setPoliceName(infoObj.policeName);
                                DetailsTool.setPoliceId(infoObj.policeId);
                                DetailsTool.setIp(infoObj.ip);
                                DetailsTool.setDeviceNum(infoObj.deviceNum);
                                DetailsTool.setUploadTime(infoObj.uploadTime);

                                // 详情创建与展示
                                DetailsTool.createDetail();
                                DetailsTool.show();
                            }
                        },
                        error: function () {
                            alertify.alert('错误！请检查网络环境！');
                        }
                    });
                }
            })
            .style("fill", function(d) {return d.target?'rgb(255, 153, 0)':'rgb(255, 223, 107)';})
            .append("title")
            .text(function(d) { return d.name; });

        node.append("text").attr("x", 12)
            .attr("dy", ".35em").text(function(d) { return d.name; });

        force.on("tick", function() {

            var nodeLocation = [];

            line.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });
            node
                .attr("transform", function(d) {
                    nodeLocation.push({x: d.x, y: d.y});
                    return "translate(" + d.x + "," + d.y + ")";
                });

            linkLabel.attr("x", function(d) { return (d.source.x + d.target.x) / 2; })
                .attr("y", function(d) { return (d.source.y + d.target.y) / 2;});


            if(commonMode) {
                circleLine.attr("x1", function(d) { return nodeLocation[d.source].x; })
                    .attr("y1", function(d) { return nodeLocation[d.source].y; })
                    .attr("x2", function(d) { return nodeLocation[d.target].x; })
                    .attr("y2", function(d) { return nodeLocation[d.target].y; });
            }
        });
    });
}


function createIMD3Viz(graph) {
    var width = 740,
        height = 550;

    var graphNodes = [],
        graphLinks = [];

    for (var i = 0; i < graph.resultItems.length; i ++) {
        var item = graph.resultItems[i];
        graphNodes = graphNodes.concat(item.nodes);
        graphLinks = graphLinks.concat(item.links);
    }

    var force = d3.layout.force()
        .charge(-600)
        .size([width, height]);

    var svg = d3.select("#showBoard").append("svg")
        .attr("width", width)
        .attr("height", height).append("g");

    d3.json("/AMD/resources/json/conjugate.json", function() {

        force
            .nodes(graphNodes)
            .links(graphLinks)
            .charge(-600)
            .linkDistance(50)
            .start();

        var link = svg.selectAll(".link")
            .data(graphLinks)
            .enter()
            .append("g")
            .attr("class", "link");

        var line = link.append("line")
            .style("stroke-width", "1px")
            .style("stroke", "gray");

        var node = svg.selectAll(".node")
            .data(graphNodes)
            .enter().append("g")
            .attr("class", "node")
            .on("click", function(event) {
                if (shiftDown) {
                    var number = event.name.trim();
                    if (number.length != 0) {
                        var w = window.open();
                        setTimeout(function() {
                            var imType = event.imType;
                            if (imType == 'qq') {
                                w.location = '/AMD/BasicQuery/detail?id=2:'+number+':bb';
                            } else if (imType == 'wx'){
                                w.location = '/AMD/BasicQuery/detail?id=3:'+number+':bb';
                            }
                        }, 1000);
                    }
                }
            })
            .call(force.drag);

        node.append("circle")
            .attr("r", function(d) {
                switch (d.nodeType) {
                    case "super":
                        return 10;  break;
                    case "normal":
                        return 10;  break;
                    case "group":
                        return 7;  break;
                    case "polymerization":
                        return 7;  break;
                    default :
                        return 10;  break;
                }
            })
            .style("fill", function(d) {
                if (d.nodeType === "normal" || d.nodeType === "super") {
                    switch (d.imType) {
                       case "qq":
                           return d.nodeType === "super"? "rgb(0, 170, 239)" : "#6BDFFF";
                       case "wx":
                           return d.nodeType === "super"? "rgb(31, 191, 65)" : "#82E042";
                       case "wb":
                           return "#FF9900";
                       default :
                           return "#333645";
                    }
                } else if (d.nodeType === "group") {
                    return "#F3FF6B";
                } else {
                    return "#C6DBEF";
                }
            }).append("title")
            .text(function(d) { return d.name; });

        node.append("text").attr("x", 12)
            .attr("dy", ".35em").text(function(d) { return d.name; });

        force.on("tick", function() {

            var nodeLocation = [];

            line.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });
            node
                .attr("transform", function(d) {
                    nodeLocation.push({x: d.x, y: d.y});
                    return "translate(" + d.x + "," + d.y + ")";
                });
        });
    });
}

function createZoneD3Viz(result) {

    var width = 740,
        height = 550;
    var diameter = 960,
        format = d3.format(",d");

    var pack = d3.layout.pack()
        .size([width - 4, height - 4])
        .value(function(d) { return d.size; });

    var svg = d3.select("#showBoard").append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(2,2)");

    d3.json("resources/json/flare.json", function(error, root) {

        root = {};
        root.name = result.rootNode.name;
        root.children = result.rootNode.children;

        var node = svg.datum(root).selectAll(".node")
            .data(pack.nodes)
            .enter().append("g")
            .attr("class", function(d) { return d.children ? "node" : "leaf node"; })
            .attr("transform", function(d) {
                console.log(d.x + '  ' + d.y );
                return "translate(" + d.x + "," + d.y + ")";
            });

        node.append("title")
            .text(function(d) { return d.name + (d.children ? "" : ": " + format(d.size)); });

        node.append("circle")
            .attr("class", "zoneCircle")
            .attr("r", function(d) { return d.r; });

        node.filter(function(d) { return !d.children; }).append("text")
            .attr("dy", ".3em")
            .style("text-anchor", "middle")
            .text(function(d) { return d.name.substring(0, d.r / 3); });
    });

    d3.select(self.frameElement).style("height", height + "px");
}
