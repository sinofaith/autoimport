/**
 * Created by zhengjiabin on 14-8-3.
 */

var global;//数据静态
function createCommunicateD3Viz(graph, commonMode) {

    var width = $("#mygraph").width(),
        height = 700;

    var color = d3.scale.category20();

    var force = d3.layout.force()
        .charge(-600)
        .size([width, height]);

    var svg = d3.select("#mygraph").append("svg")
        .attr("width", width)
        .attr("height", height).append("g");

    d3.json("/AMD/resources/json/conjugate.json", function(error, temp) {
        var linksData = commonMode?graph.links:graph.polyLinks;
        force
            .nodes(graph.nodes)
            .links(linksData)
            .linkDistance(function(d) {
                return 400 * (1 / d.factor) + 10;
            })
            .start();

        var link = svg.selectAll(".link")
            .data(linksData)
            .enter()
            .append("g")
            .attr("class", "link");

        var line = link.append("line")
            .attr('class', 'communicateNormalLine')
            .attr("sourceNum", function(d) {
                return d.sourceNum;
            })
            .attr("targetNum", function(d) {
                return d.targetNum;
            });

        var linkLabel = link.append("text")
            .attr("class", "linkLabel linkShowTitle")
            .attr("x", function(d) {
                return (d.source.x + d.target.x) / 2;
            })
            .attr("y", function(d) {
                return (d.source.y + d.target.y) / 2;
            })
            .attr("fill", "#666666")
            .text(function(d) { return d.count;});

        linkLabel.append("title")
            .text(function(d) {
                return "通话记录:" + d.callCount + "    短信记录:" + d.smsCount;
            });

        var circleLink, circleLine, circleLineLabel;
        if (commonMode) {
            circleLink = svg.selectAll(".cricleLine")
                .data(graph.circleLinks)
                .enter().append("g");
            circleLine = circleLink.append("line")
                .attr("class", "communicatePolyLine")
                .attr("sourceNum", function(d) {
                    return d.sourceNum;
                })
                .attr("targetNum", function(d) {
                    return d.targetNum;
                });
        }

        var node = svg.selectAll(".node")
            .data(graph.nodes)
            .enter().append("g")
            .attr("class", "node")
            .call(force.drag);

        node.append("image")
            .attr("height", function(d) {
                return d.target? 70 : 45;
            })
            .attr("width", function(d) {
                return d.target? 30 : 20;
            })
            .attr("xlink:href", function(d) {
                return d.recorded? "/AMD/resources/image/phone1.png":
                    "/AMD/resources/image/phone1.png";
            })
            .attr('transform', 'translate(-20, -20)')
            .attr('imageflag', true)
            .on("click", function(event) {
                console.log(event);
                if(selected!=null){
                    selected.selected = false;
                }
                selected=event;
                if(event.selected){
                    event.selected = false;
                }else
                    event.selected=true;
                    tick();
                    if (altDown) {
                        centering(this, $('#mygraph>svg'));
                    }
            });
            //.append("title")
            //.text(function(d) { return d.name; });

        node.append("text").attr("x", 12)
            .attr("dy", ".35em").text(function(d) { return d.name; });

        force.on("tick", tick);
        //顶点固定
        setTimeout(function(){setStatic();},2000);//顶点固定
        $('#unstatic').click(function(){
            unStatic();
        });
        //到这里
        function tick(){
            var nodeLocation = [];

            line.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });
            node
                .attr("transform", function(d) {
                    nodeLocation.push({x: d.x, y: d.y});
                    return "translate(" + d.x + "," + d.y + ")";
                })
                .select("image")


                .attr("xlink:href", function(d) {
                    if(d.selected){
                        if(d.recorded) return "/AMD/resources/image/select_phone1.png";
                        else return "/AMD/resources/image/select_phone1.png";
                    }else{
                        return d.recorded? "/AMD/resources/image/phone1.png":
                            "/AMD/resources/image/phone1.png";
                    }
                });

                //.classed('selected',function(d){return d.selected});
            linkLabel.attr("x", function(d) { return (d.source.x + d.target.x) / 2; })
                .attr("y", function(d) { return (d.source.y + d.target.y) / 2;});


            if(commonMode) {
                circleLine.attr("x1", function(d) { return nodeLocation[d.source].x; })
                    .attr("y1", function(d) { return nodeLocation[d.source].y; })
                    .attr("x2", function(d) { return nodeLocation[d.target].x; })
                    .attr("y2", function(d) { return nodeLocation[d.target].y; });
            }
        }
    });
}


function createIMD3Viz(graph) {
    var width = $("#mygraph").width(),
        height = 600;

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

    var svg = d3.select("#mygraph").append("svg")
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
            .attr('class', 'IMLine')
            .attr("sourceNum", function(d) {
                return d.sourceNum == null? "" : d.sourceNum;
            })
            .attr("targetNum", function(d) {
                return d.targetNum == null? "" : d.targetNum;
            });

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
                } else if (altDown) {
                    centering(this, $('#mygraph>svg'));
                }
            })
            .call(force.drag);

        node.append("image")
            .attr("height", function(d) {
                return d.nodeType == 'super'? 60 : 40;
            })
            .attr("width", function(d) {
                return d.nodeType == 'super'? 60 : 40;
            })
            .attr("xlink:href", function(d) {
                if (d.nodeType == "normal" || d.nodeType == "super") {
                    switch (d.imType) {
                        case "qq" :
                            return "/AMD/resources/image/qq.png";
                        case "wx":
                            return "/AMD/resources/image/weichart.png";
                    }
                }

                return "";
            })
            .attr("display", function(d) {
                return d.nodeType == "polymerization"? "none":"block";
            })
            .attr('transform', 'translate(-20, -20)')
            .attr('imageflag', true);
            //.append("title").text(function(d){
            //    return d.name;
            //});

        node.append("circle")
            .attr("r", 6)
            .style("fill", "#99CCFF")
            .style("display", function(d) {
                if (d.nodeType == "polymerization") {
                    return "block";
                } else {
                    return "none";
                }
            }).append("title")
            .text(function(d) { return d.tooltip; });

        node.append("text").attr("x", 12)
            .attr("dx", "2em").text(function(d) { return d.name; });

        force.on("tick", tick);
        //顶点固定
        setTimeout(function(){setStatic(graph);},2000);//顶点固定
        $('#unstatic').click(function(){
            unStatic(graph);
        });
        //结束
        function tick(){
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
        }

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

    var svg = d3.select("#mygraph").append("svg")
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
