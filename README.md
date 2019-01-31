## 50.001 - 1D Application Project
#Smart Mirror

![images/logo](https://github.com/SarthakG16/50001_1D/blob/master/SmartMirrorApp/images/logo.JPG =500x "Smart Mirror")

"CI03 Group 6*
Andre Hadianto Lesmana | Antonio Miguel Canlas Quizon | Ryan Yu Jun An | Sarthak Ganoorkar | Tan Zhao Tong | Yee Celine

### Problem
As an institute that boasts of high-end technology and innovative design, our team felt that the lifts on campus are unimpressive and 
not representative of our school values. The elevators have clasps for paper posters however, due to the manual nature of such a 
system, it brings about a myriad of problems. People who want to promote their activities by means of posters have to first seek ROOT 
for approval, which is a long and arduous process. Upon approval, they have to manually print the poster and put them up in the lifts 
one by one. This is a waste of paper and makes the lifts very cluttered when people neglect to take their posters down after the 
event. 

### Solution
Our group aims to digitise this process via a phone application and installing smart mirrors or displays in the lifts. Our solution 
will make a key space on campus both clean and technologically-infused. It will also reduce unnecessary labour and wastage in managing, 
printing, and changing posters. We believe that this system would benefit all parties involved, as well as add vibrancy to the campus. 
As we use elevators multiple times every day, and we believe that this could be the best way to impress visitors.
Our project consists of three components: an Android application, a Python Flask backend and a smart display powered by a Raspberry Pi.

![images/Project Overview](https://github.com/SarthakG16/50001_1D/blob/master/SmartMirrorApp/images/poster.jpg "Smart Mirror")

#### Android Application
Our application acts as a management application that allows students and staff to upload their posters to the smart mirrors. 
Administrators, or admins, will also be able to approve or reject the posters upon checking them within the application. 
Non-administrators (referred to as ‘guests’ in the rest of this report) will be able to manage and view their posters through the 
application. <br/>
The following figure shows the process, UI and functions of the application.
![images/app_flow](https://github.com/SarthakG16/50001_1D/blob/master/SmartMirrorApp/images/app_flow.jpg "App Flow")

#### Backend
The backend was written in Python and uses the Flask framework. It uses an SQLite database that stores all the informations of the 
posters and accounts. Both the Android application and the Raspberry Pi queries the API exposed by the backend to obtain the required 
information, as well as update/add poster details. The source code can be found [here](https://github.com/ryan-gr/50001_1D_Backend)

#### Mirror Interface
We decided to use a Raspberry Pi as opposed to an Android device, as we felt that the Pi was more well suited for a stationary device 
such as a smart mirror.  Also, part of our design includes an energy saving feature for our display, which uses proximity infrared (PIR) 
sensors, justifying the necessity of the Raspberry Pi. This also leaves us room for future expansion, allowing us to incorporate other 
sensors to add other features to the smart mirror.
We decided to use Node.js as there is already an existing open source smart mirror platform called ‘MagicMirror’ for programmers that are 
interested in developing smart mirrors. ‘Magic Mirror’ comes with pre-designed interface modules. Modules are ‘blocks’ of interfaces that 
have specific functions in the smart mirror UI. Certain modules are defaulted in the platform such as clock, date and weather modules. 
However, as our smart mirror engages on advertising of posters, we had to program our own module. This also allows us an additional 
platform for expansion, allowing us to design new modules to add even more features to the smart mirror in the future. <br/>
The functionality of the smart mirror is convenient. The Raspberry Pi queries the server, downloading the poster data of ‘posted’ posters. 
The image data is then converted to an image file, since the server accepts, stores and returns the poster as serialized image data. Finally, 
we set this process to be repeated on intervals of 6 hours to keep the smart mirror interface updated.
The interface code can be found [here](https://github.com/migsquizon/Glance-Interface-2018)

#### Future Plans
Our first step is to get a working prototype to be set-up in SUTD (current prototype was collected by Living Labs Department for future 
development), and then further refine it according the users’ feedback. Our vision for Glance, given better funding, is to create a more 
feature-rich smart mirror, by either replacing the Raspberry Pi with a faster linux based device such as Lattepanda, or optimizing the current 
script being used. With this, we can also implement an interactive feature using IR sensors to sense hand-signals such as waving left/right to 
control the posters displayed on the screen. 
Application-wise, we can further develop it through adding functions such as uploading video advertisements or the ability to view and control 
poster displays for administrators, or by increasing the frequency of certain posters at certain times (e.g. food-related events nearer to meal 
times).

### Conclusion
Through this project, we have applied good and effective software programming practices we’ve learnt in class such as object-oriented design, 
polymorphism, inheritance, and Android programming. Overall, our project was able to integrate numerous processes into one Android application. 
From the automation of ‘posting’ to one-step approval for administrators, this project is a step forward to SUTD as a smart campus and becoming 
the Smart Nation that Singapore aspires to achieve. We believe that this project will revolutionise advertising in schools, workplaces, and 
community spaces.<br/>