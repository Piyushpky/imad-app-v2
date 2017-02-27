 //button code
 var button=document.getElementById('counter');
 
 button.onclick=function(){
 var request =new XMLHttpRequest();
 request.onreadystatechange = function(){
    if(request.readystate== XMLHttpRequest.DONE){
        if(request.status==200){
            var counter=response.responseText;
            var span = document.getElementById('count');
            span.innerHTMl=counter.toString();
        }
    }     
 }
 request.open('GET','http://piyushpky.imad.hasura-app.io/counter',true);
 request.send(null);
 };