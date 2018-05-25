
    function addlink(){
        var Exp =/[a-zA-z]+:\/\/[^\s]*/g;

        content = 'http://wew  你是事 http://dede !dcdhttp://rerne';
        newcontent = content.replace(Exp,'<a href="javascript:void(0);">$&</a>');
        $('.link').append(newcontent);
    }

