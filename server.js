var express = require('express');
var morgan = require('morgan');
var path = require('path');
var pool=require('pg').Pool;

var config={
    user:'piyushpky',
    database:'piyushpky',
    host:'db.imad.harura-app.io',
    port:'5432',
    password:process.env.D8_PASSWORD
    
}


var app = express();
app.use(morgan('combined'));


var articles={
article1:{
  
  heading:'Article one!',
  date: 'Sep 5, 2016',
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
article2:{
    
  heading:'Article one!',
 date: 'Sep 5, 2016',
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
article3:{
  
  heading:'Article one!',
  date: 'Sep 5, 2016',
  content:`<p>Here will be the contents of this page.
                Here will be the contents of this page.
                Here will be the contents of this page.
                </p>
            </div>
            
                </p>`}
};
function createTemplate(data){

var date=data.date;
var content=data.content;
var heading=data.heading;
var htmlTemp=`<html>
    
    <head>
        <title>'title'</title>
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

var counter=0;
app.get('/counter',function(req,res){
    counter=counter+1;
    res.send(counter.toString());
});
app.get('/', function (req, res) {
  res.sendFile(path.join(__dirname, 'ui', 'index.html'));
});

var pool=new Pool(config); 
app.get('/test-db',function(req,res){
   //make a select request
   //return a response with the results
   pool.query('SELECT * FROM test',function(req,res){
       if(err){
           res.status(500).send(err.toString());
       }else{
           res.send(JSON.stringify(result));
       }
   });
   
});
app.get('/article/:articleName',function(req,res){
    pool.query("SELECT * FROM article WHERE title='"+req.params.articleName+"'",function(err,result){
        if(err){
            res.status(500).send(err.toString());
            
        }else{
            if(result.rows.length===0){
                result.status(404).send('article Not Found');
            }else{
                var  articleData=result.rows[0];
                res.send(createTemplate(articleData));
            }
        }
    });
   
});
var counter=0;


app.get('/ui/style.css', function (req, res) {
  res.sendFile(path.join(__dirname, 'ui', 'style.css'));
});

app.get('/ui/main.js', function (req, res) {
  res.sendFile(path.join(__dirname, 'ui', 'main.js'));
});

app.get('/ui/10414834_897251250334877_162128951368834263_n.jpg', function (req, res) {
  res.sendFile(path.join(__dirname, 'ui', '10414834_897251250334877_162128951368834263_n.jpg'));
});


var port = 8080; // Use 8080 for local development because you might already have apache running on 80
app.listen(8080, function () {
  console.log(`IMAD course app listening on port ${port}!`);
});
