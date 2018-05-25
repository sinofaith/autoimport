/**
 * Created by zhengjiabin on 12/17/15.
 */
$(function() {

    $('#btn1001').click(function() {
        dataPreHandle(data_1001);
        draw(data_1001)
    });

    $('#btn1101').click(function() {
        dataPreHandle(data_1101);
        draw(data_1101);
    });

    function dataPreHandle(data) {
        _.each(data.nodes, function(node) {
            node.label = node.id;
            node.image = '/AMD/resources/image/phone1.png';
            node.shape ='image';
            node.size = node.input? 15: 8;
            if (node.first) {
                node.borderWidth = 2;
                node.shapeProperties = {
                    useBorderWithImage: true
                };
                node.color = {
                    background: 'transparent',
                    border: 'orange'
                };
            } else if (!node.input) {
                node.borderWidth = 2;
                node.shapeProperties = {
                    useBorderWithImage: true
                };
                node.color = {
                    background: 'transparent',
                    border: 'red'
                };
            }

            if (node.poison) {
                node.font = {
                    strokeWidth: 2,
                    strokeColor: 'green'
                };
            }
        });

        _.each(data.edges, function(edge) {
            edge.color = {
                color: '#848688'
            }
        });
    }
    function draw(data) {
        // create a network
        var container = document.getElementById('visContainer');
        var options = {};
        network = new vis.Network(container, data, options);
        network.on('doubleClick', function(params) {
            if (params.nodes.length > 0) {
                var node = _.findWhere(data.nodes, {
                    id: params.nodes[0]
                });
                node.address = node.address? node.address: '未知';
                node.name = node.name? node.name: '未知';
                node.gmsfhm = node.gmsfhm? node.gmsfhm: '未知';
                swal({
                    title: '',
                    text: "<div style='text-align: left;margin-left: 120px;'>姓名: " + node.name + '</div><br>' +
                    "<div style='text-align: left;margin-left: 120px;'>身份证: " + node.gmsfhm + '</div><br>' +
                    "<div style='text-align: left;margin-left: 120px;'>手机号: " + node.id + '</div><br>' +
                    "<div style='text-align: left;margin-left: 120px;'>地址: " + node.address + '</div>',
                    html: true
                })
            }
        });
    }

});