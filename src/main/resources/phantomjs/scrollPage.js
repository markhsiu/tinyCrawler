system = require('system')   
address = system.args[1];//获得命令行第二个参数 接下来会用到   
var page = require('webpage').create();   
var url = address;   

page.open(url, function (status) {
    if (status != "success"){
        console.log('FAIL to load the address');
        phantom.exit();
    }
    
    page.evaluate(function(){
        //此函数在目标页面执行的，上下文环境非本phantomjs，所以不能用到这个js中其他变量
        
        window.scrollTo(0,10000);//滚动到底部
        //window.document.body.scrollTop = document.body.scrollHeight;
    });   

});