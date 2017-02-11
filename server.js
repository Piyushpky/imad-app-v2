var express = require('express');
var morgan = require('morgan');
var path = require('path');

var app = express();
app.use(morgan('combined'));

var articles={
article1:{
  title:'Ar1',
  heading:'Article one!',
  data: '10th Feb 2017',
  content:`<p>Here will be the contents of this page.
                Here will be the contents of this page.
                Here will be the contents of this page.
                </p>
            </div>
            
            <div>
                <p>Here will be the contents of this page.
                Here will be the contents of this page.
                Here will be the contents of this page.
                </p>
            </div>
            
            <div>
                <p>Here will be the contents of this page.
                Here will be the contents of this page.
                Here will be the contents of this page.
                </p>`
},
article2:{title:'Ar1',
  heading:'Article one!',
  data: '10th Feb 2017',
  content:`<p>Here will be the contents of this page.
                Here will be the contents of this page.
                Here will be the contents of this page.
                </p>
            </div>
       
            <div>
                <p>Here will be the contents of this page.
                Here will be the contents of this page.
                Here will be the contents of this page.
                </p>`},
article3:{title:'Ar1',
  heading:'Article one!',
  data: '10th Feb 2017',
  content:`<p>Here will be the contents of this page.
                Here will be the contents of this page.
                Here will be the contents of this page.
                </p>
            </div>
            
                </p>`}
};
function createTemplate(data){
var title=data.title;
var date=data.date;
var content=data.content;
var heading=data.heading;
var htmlTemp=`<html>
    
    <head>
        <title>${title}</title>
        </head>    
        <body>
            <div>
                <a href="/">HOME</a>
            </div>
            <hr/>
            <h3>${heading}</h3>
            <div>
                ${date}
            </div><div>
            ${content}</div>
            </body>
    </html>

`;
return htmlTemp;
}
app.get('/', function (req, res) {
  res.sendFile(path.join(__dirname, 'ui', 'index.html'));
});
app.get('/:articleName',function(req,res){
    var articleName=req.params.articleName;
   res.send(createTemplate(articles[articleName]));
});
var counter=0;
app.get('/counter',function(req,res){
    counter=counter+1;
    res.send(counter.toString());
})

app.get('/ui/style.css', function (req, res) {
  res.sendFile(path.join(__dirname, 'ui', 'style.css'));
});

app.get('/ui/main.js', function (req, res) {
  res.sendFile(path.join(__dirname, 'ui', 'main.js'));
});

app.get('/ui/madi.png', function (req, res) {
  res.sendFile(path.join(__dirname, 'ui', 'madi.png'));
});


var port = 8080; // Use 8080 for local development because you might already have apache running on 80
app.listen(8080, function () {
  console.log(`IMAD course app listening on port ${port}!`);
});
