/*
	Copyright (c) 2004-2011, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/

//>>built
define("dojo/request/node","require ./util ./handlers ../errors/RequestTimeoutError ../node!http ../node!https ../node!url ../node!stream".split(" "),function(t,g,k,l,m,n,p,q){function f(d,a){var b=g.parseArgs(d,g.deepCreate(r,a),a&&a.data instanceof s);d=b.url;a=b.options;var e=g.deferred(b,function(a,b){b.clientRequest.abort()});d=p.parse(d);var c=b.requestOptions={hostname:d.hostname,port:d.port,socketPath:a.socketPath,method:a.method,headers:a.headers,agent:a.agent,pfx:a.pfx,key:a.key,passphrase:a.passphrase,
cert:a.cert,ca:a.ca,ciphers:a.ciphers,rejectUnauthorized:!1===a.rejectUnauthorized?!1:!0};d.path&&(c.path=d.path);if(a.user||a.password)c.auth=(a.user||"")+":"+(a.password||"");c=b.clientRequest=("https:"===d.protocol?n:m).request(c);if(a.socketOptions&&("timeout"in a.socketOptions&&c.setTimeout(a.socketOptions.timeout),"noDelay"in a.socketOptions&&c.setNoDelay(a.socketOptions.noDelay),"keepAlive"in a.socketOptions)){var f=a.socketOptions.keepAlive;c.setKeepAlive(0<=f,f||0)}c.on("socket",function(){b.hasSocket=
!0;e.progress(b)});c.on("response",function(a){b.clientResponse=a;b.status=a.statusCode;b.getHeader=function(b){return a.headers[b.toLowerCase()]||null};var c=[];a.on("data",function(a){c.push(a)});a.on("end",function(){h&&clearTimeout(h);b.text=c.join("");try{k(b),e.resolve(b)}catch(a){e.reject(a)}})});c.on("error",e.reject);a.data?"string"===typeof a.data?c.end(a.data):a.data.pipe(c):c.end();if(a.timeout)var h=setTimeout(function(){e.cancel(new l(b))},a.timeout);return e.promise}var s=q.Stream,
r={method:"GET",query:null,data:void 0,headers:{}};g.addCommonMethods(f);return f});