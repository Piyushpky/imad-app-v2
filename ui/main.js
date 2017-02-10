console.log('Loaded!');
var element=document.getElementById('maint');
element.innerHTML='new Value';
var img=document.getElementById('mad');
var marginLeft=0;
function moveRight(){
    marginLeft=marginLeft+10;
    img.style.marginLeft=marginLeft+'px';
}
img.onclick=function(){
    var interval=setInterval(moveRight,100);
    
};