$(document).ready(function() {
    alertify.defaults = {
        // dialogs defaults
        modal:true,
        movable:true,
        resizable:true,
        closable:true,
        maximizable:true,
        pinnable:true,
        pinned:true,
        padding: true,
        overflow:true,
        maintainFocus:true,
        transition:'pulse',

        // notifier defaults
        notifier:{
            // auto-dismiss wait time (in seconds)
            delay:5,
            // default position
            position:'bottom-right'
        },

        // language resources
        glossary:{
            // dialogs default title
            title:'提示',
            // ok button text
            ok: '确定',
            // cancel button text
            cancel: '取消'
        },

        // theme settings
        theme:{
            // class name attached to prompt dialog input textbox.
            input:'ajs-input',
            // class name attached to ok button
            ok:'ajs-ok',
            // class name attached to cancel button
            cancel:'ajs-cancel'
        }
    };
});