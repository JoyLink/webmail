inbox(user) ::= <<
            <!DOCTYPE HTML>
                  <html>
                   <head>
                    <title>Index</title>
                     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                     <link href="/files/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
                    <link href="/files/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
                     <link href="/files/assets/css/main-min.css" rel="stylesheet" type="text/css" />
                   </head>
                   <body>

                    <div class="header">

                        <div class="dl-title">
                          <a href="" title="" target="_blank">
                            <span class="lp-title-port">Th111</span><span class="dl-title-text">Webmail</span>
                          </a>
                        </div>

                      <div class="dl-log">
                      <script>
                      var x="";
                      var time=new Date().getHours();
                      if (time<=12&&time>0)
                        {
                        x="Good Morning";
                        }
                      else if(time >12 && time<=18) x = "Good Afternoon";
                      else x = "Good Evening"  ;
                        document.write(x);
                      </script>
                      <span class="dl-log-user">$user$ !</span><a href="/logout" title="Logout" class="dl-log-quit">[Logout]</a>    </div>
                    </div>
                     <div class="content">
                      <div class="dl-main-nav">
                        <div class="dl-inform"><div class="dl-inform-title"><s class="dl-inform-icon dl-up"></s></div></div>
                        <ul id="J_Nav"  class="nav-list ks-clear">
                          <li class="nav-item dl-selected"><div class="nav-item-inner nav-home">Mail</div></li>
                        </ul>
                      </div>
                      <ul id="J_NavContent" class="dl-tab-conten">

                      </ul>
                     </div>
                    <script type="text/javascript" src="/files/assets/js/jquery-1.8.1.min.js"></script>
                    <script type="text/javascript" src="/files/./assets/js/bui.js"></script>
                    <script type="text/javascript" src="/files/./assets/js/config.js"></script>

                    <script>
                      BUI.use('common/main',function(){
                        var config = [{
                            id:'menu',
                            homePage : 'code',
                            menu:[{
                                text:'Management',
                                items:[
                                  {id:'sendmail-menu',text:'Write', href:'files/main/write-menu.html'},
                                  {id:'code',text:'Inbox',href:'/files/main/showinbox.html',closeable : true},
                                  {id:'main-menu',text:'Outbox',href:'/files/main/outbox.html'},
                                  {id:'second-menu',text:'Trash',href:'files/main/second-menu.html'},

                                  {id:'dyna-menu',text:'Setting',href:'files/main/dyna-menu.html'}
                                ]
                              }]
                            }];
                        new PageUtil.MainPage({
                          modulesConfig : config
                        });
                      });
                    </script>
                   </body>
                  </html>

>>