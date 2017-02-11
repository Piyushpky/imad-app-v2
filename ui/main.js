 //button code
 var button=document.getElementById('counter');
 var counter=0;
 button.onclicked=function(){
   counter=counter+1;
   var span=document.getElementById('count');
   span.innerHTML=counter.toString();
 };