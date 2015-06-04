# sudoor-server-lib

## What's sudoor-server-lib
sudoor-server-lib is a lightweight enterprise grade application backend solution. 

## Why sudoor-server-lib
From the beginning I started this project, 4 principles: Lightweight, Security, Performance & Automation keep in my mind to guide the direction of the future of this project.


### Lightweight
We all know that Java shines a lot the development of Enterprise Application. With this great tool we can build much larger and stronger application than before. JavaEE seems to be the best choice for enterprise application standard ever.
With these glories accumulate, the JaveEE turn to be more and more heavier. It's time to change.
Sudoor base on JaveEE, but try his best to avoid the word "heavy". 

#### Minimal configurations
Through all over the application, which base on Sudoor, you can only find only one configuration file: application.properties. We tried our best to keep all the configurations centrally maintained. As a full featured web application, there is even no web.xml. About configuration, I think you can not remove any more.

#### We dropped all unnecessary stuff
We removed servlets and filters, we only love POJO so everything in your program is a POJO. This feature also make it extremely portable.
You may don't like websphere/weblogic, because it's not only charged but also very heavy. You even need to have a dedicated engineer to administrator it. 
May people walk away from websphere/weblogic and walk into the lovely tomcat. Since it's much lighter, even newbies can easily get it up. So cheers.
Now we take one more step forward, we dropped stand alone tomcat, we embed it in to your war. So that you even no need to navigate to apache, search the release and download. 
You no need to know how to start tomcat, it will automatically up when you run your app. 


### Security
Out-of-box support resistance of Session Fixation, XSS, CSRF, SQL Injection attack. You have to do nothing but enjoy your coffee. Meanwhile more and more attack prevention will added in future.

### Performance
The most important part to improve a web application's performance should be the cache. 
Here we are using multiple layer cache architecture.
In first layer, we are using distributed cache provider Infinispan as ORM 2LC to minimize the expensive DB access.
In second layer, we are using Redis to cache use's session data. With this cache you can cache some result to avoid a lot of unnecessary duplicated computation. 
In third layer, we are using Nginx cache to serve static content. Which means that most of requests are served directly by high performance web server nginx, no need to come to application server.
In fourth layer, we utilize browser cache to combine with cache bust, to make it possible to safely save static content in browser without request send to the network. 

### Automation
In traditional IT company, it usually take 1-3 days to prepare a release and promote to SIT, UAT and finally PRD. I could be very time consuming especially when you come to test stage. 
Here with Sudoor, which integrated with Continuous Delivery, it only need one click to get your code from SCM to PRD. All processes will be completed in less than 20 mins. This will definitely save you from the endless change-build-deploy process. 
And most importantly, all processes are done automatically, no mistake any more. 



