/**
 * Created by Achol.Xu on 2015/1/19.
 */
$(function() {
    $('div.tab a').click(function(){
        if($(this).parent().hasClass('active')) return;
        $('li').removeClass('active');
        $(this).parent().addClass('active');
        var param = $(this).attr('param')
        console.log(param);
        $('.table_content').hide();
        $('#'+param).show().children(0).show();
    });
});

//月度表格
function loadmonthtable(){
    var date = $('#myTimePicker').val();
    var arr=date.split("-");
    var title=arr[0]+"年"+arr[1]+"月 绩效考核";
    //超级暴力的重写加载
    $("#monthlyListContainer").empty().html("<table id='list2' ></table><div id='pager2'></div>");
    $("#list2").jqGrid({
        url:'performance/monthresult?condition='+date,
        datatype: "json",
        colNames:['编号','单位名称','历史关联绩效','查询使用绩效','绩效综合'],
        height: "auto",
        autowidth:true,
        colModel:[
            {name:'code',index:'code',  sorttype:"int"},
            {name:'name',index:'name', sorttype:"text"},
            {name:'historyrelation',index:'historyrelation', sorttype:"int"},
            {name:'queryperformance',index:'queryperformance', sorttype:"int"},
            {name:'total',index:'total', sorttype:"int"}
        ],
        rowNum:25,
        rowList:[10,25,50,100],
        pager: '#pager2',
        sortname: 'code',
        sortorder: "desc",
        loadonce: true,
        caption: title,
        loadComplete : function() {
            //使当前页已选中行勾选
//            setSelectedStatus();
            var table = this;
            setTimeout(function(){
                styleCheckbox(table);
                updateActionIcons(table);
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
        }
    });
    $("#list2").jqGrid('navGrid',"#pager2",
        { 	//navbar options
            edit: false,
            editicon : 'icon-pencil blue',
            add: false,
            addicon : 'icon-plus-sign purple',
            del: false,
            delicon : 'icon-trash red',
            search: true,
            searchicon : 'icon-search orange',
            refresh: false,
            refreshicon : 'icon-refresh green',
            view: false,
            viewicon : 'icon-zoom-in grey'
        },
        {
            //edit record form
            //closeAfterEdit: true,
            recreateForm: true,
            beforeShowForm : function(e) {
                var form = $(e[0]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                style_edit_form(form);
            }
        },
        {
            //new record form
            closeAfterAdd: true,
            recreateForm: true,
            viewPagerButtons: false,
            beforeShowForm : function(e) {
                var form = $(e[1]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                style_edit_form(form);
            }
        },
        {
            //delete record form
            recreateForm: true,
            beforeShowForm : function(e) {
                var form = $(e[0]);
                if(form.data('styled')) return false;

                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                style_delete_form(form);

                form.data('styled', true);
            },
            onClick : function(e) {
                alert(1);
            }
        },
        {
            //search form
            recreateForm: true,
            afterShowSearch: function(e){
                var form = $(e[0]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
                style_search_form(form);
            },
            afterRedraw: function(){
                style_search_filters($(this));
            }
//                ,
//                multipleSearch: true,
            /**
             multipleGroup:true,
             showQuery: true
             */
        },
        {
            //view record form
            recreateForm: true,
            beforeShowForm: function(e){
                var form = $(e[0]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
            }
        }
    )
}

    //加载单位表格
function loadunittable(){
    //超级暴力的重写加载
    $("#unitListContainer").empty().html("<table id='list3' ></table><div id='pager3'></div>");
    $("#list3").jqGrid({
        url:'performance/unitresult?condition='+$("#unit-search-input").val(),
        datatype: "json",
        colNames:['编号','单位名称'],
        height: "auto",
        autowidth:true,
        colModel:[
            {name:'code',index:'code',  sorttype:"int"},
            {name:'name',index:'name', sorttype:"text"}
        ],
        rowNum:25,
        rowList:[10,25,50,100],
        pager: '#pager3',
        sortname: 'code',
        sortorder: "desc",
        loadonce: true,
        caption: "单位列表",
        onSelectRow: function (rowId, status, e) {
            var selectedRow = $("#list3").jqGrid("getRowData", rowId);
            var code = selectedRow["code"];
            var name=selectedRow["name"];
            var option={};
            option.code=code;
            option.cascade=false;
            $('body').mask('正在拼命创建统计图中，请稍候...');
            $.ajax({
                url : '/AMD/performance/chartData',
                type: 'POST',
                data: JSON.stringify(option),
                dataType: 'json',
                contentType: 'application/json;charset=UTF-8',
                success: function(result) {
                    window.__VIZ_STATISTICS_DATA = undefined;
                    console.log(result);
                    ChartModule.setting('#highchartsContainer', name+'近12个月绩效', result);
                    ChartModule.createPerformanceLineChart();
                    $('body').unmask();
                },
                error: function() {
                    $('body').unmask();
                    alertify.alert('错误！请检查输入条件或网络环境！');
                }
            });
        },
        loadComplete : function() {
            //使当前页已选中行勾选
//            setSelectedStatus();
            var table = this;
            setTimeout(function(){
                styleCheckbox(table);
                updateActionIcons(table);
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
        }
    });
    $("#list3").jqGrid('navGrid',"#pager3",
        { 	//navbar options
            edit: false,
            editicon : 'icon-pencil blue',
            add: false,
            addicon : 'icon-plus-sign purple',
            del: false,
            delicon : 'icon-trash red',
            search: false,
            searchicon : 'icon-search orange',
            refresh: false,
            refreshicon : 'icon-refresh green',
            view: false,
            viewicon : 'icon-zoom-in grey'
        },
        {
            //edit record form
            //closeAfterEdit: true,
            recreateForm: true,
            beforeShowForm : function(e) {
                var form = $(e[0]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                style_edit_form(form);
            }
        },
        {
            //new record form
            closeAfterAdd: true,
            recreateForm: true,
            viewPagerButtons: false,
            beforeShowForm : function(e) {
                var form = $(e[1]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                style_edit_form(form);
            }
        },
        {
            //delete record form
            recreateForm: true,
            beforeShowForm : function(e) {
                var form = $(e[0]);
                if(form.data('styled')) return false;

                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                style_delete_form(form);

                form.data('styled', true);
            },
            onClick : function(e) {
                alert(1);
            }
        },
        {
            //search form
            recreateForm: true,
            afterShowSearch: function(e){
                var form = $(e[0]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
                style_search_form(form);
            },
            afterRedraw: function(){
                style_search_filters($(this));
            }
//                ,
//                multipleSearch: true,
            /**
             multipleGroup:true,
             showQuery: true
             */
        },
        {
            //view record form
            recreateForm: true,
            beforeShowForm: function(e){
                var form = $(e[0]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
            }
        }
    )

}

function style_edit_form(form) {
    //enable datepicker on "sdate" field and switches for "stock" field
    form.find('input[name=sdate]').datepicker({format:'yyyy-mm-dd' , autoclose:true})
        .end().find('input[name=stock]')
        .addClass('ace ace-switch ace-switch-5').wrap('<label class="inline" />').after('<span class="lbl"></span>');

    //update buttons classes
    var buttons = form.next().find('.EditButton .fm-button');
    buttons.addClass('btn btn-sm').find('[class*="-icon"]').remove();//ui-icon, s-icon
    buttons.eq(0).addClass('btn-primary').prepend('<i class="icon-ok"></i>');
    buttons.eq(1).prepend('<i class="icon-remove"></i>')

    buttons = form.next().find('.navButton a');
    buttons.find('.ui-icon').remove();
    buttons.eq(0).append('<i class="icon-chevron-left"></i>');
    buttons.eq(1).append('<i class="icon-chevron-right"></i>');
}
function style_delete_form(form) {
    var buttons = form.next().find('.EditButton .fm-button');
    buttons.addClass('btn btn-sm').find('[class*="-icon"]').remove();//ui-icon, s-icon
    buttons.eq(0).addClass('btn-danger').prepend('<i class="icon-trash"></i>');
    buttons.eq(1).prepend('<i class="icon-remove"></i>')
}
function style_search_filters(form) {
    form.find('.delete-rule').val('X');
    form.find('.add-rule').addClass('btn btn-xs btn-primary');
    form.find('.add-group').addClass('btn btn-xs btn-success');
    form.find('.delete-group').addClass('btn btn-xs btn-danger');
}
function style_search_form(form) {
    var dialog = form.closest('.ui-jqdialog');
    var buttons = dialog.find('.EditTable')
    buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info').find('.ui-icon').attr('class', 'icon-retweet');
    buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse').find('.ui-icon').attr('class', 'icon-comment-alt');
    buttons.find('.EditButton a[id*="_search"]').addClass('btn btn-sm btn-purple').find('.ui-icon').attr('class', 'icon-search');
}
function styleCheckbox(table) {
    /**
     $(table).find('input:checkbox').addClass('ace')
     .wrap('<label />')
     .after('<span class="lbl align-top" />')


     $('.ui-jqgrid-labels th[id*="_cb"]:first-child')
     .find('input.cbox[type=checkbox]').addClass('ace')
     .wrap('<label />').after('<span class="lbl align-top" />');
     */
}
//unlike navButtons icons, action icons in rows seem to be hard-coded
//you can change them like this in here if you want
function updateActionIcons(table) {
    /**
     var replacement =
     {
         'ui-icon-pencil' : 'icon-pencil blue',
         'ui-icon-trash' : 'icon-trash red',
         'ui-icon-disk' : 'icon-ok green',
         'ui-icon-cancel' : 'icon-remove red'
     };
     $(table).find('.ui-pg-div span.ui-icon').each(function(){
						var icon = $(this);
						var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
						if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
					})
     */
}

//replace icons with FontAwesome icons like above
function updatePagerIcons(table) {
    var replacement =
    {
        'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
        'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
        'ui-icon-seek-next' : 'icon-angle-right bigger-140',
        'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
    })
}
function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container:'body'});
    $(table).find('.ui-pg-div').tooltip({container:'body'});
}

