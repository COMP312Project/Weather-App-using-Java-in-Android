# Weather App
Our goal is to update a weather app (in Java) to show a 5-day forecast, search for weather by city, fetch weather data
(temperature, humidity, wind speed, etc.), and provide severe weather alerts.

## Mission Statement
Our mission is to collaboratively develop a weather app that enhances our understanding of open-source software 
development. Through teamwork, experimentation, and real-world coding practices, we aim to create a functional and 
user-friendly tool that serves as a learning resource for ourselves and our classmates.


## Project Contribution Summary

For this project, we contributed to a pre-existing open-source weather app built with Java and Android. Our primary
contributions focused on integrating an API key from OpenWeatherMap, making some improvements along with 
documentation and improving code clarity for better maintainability.

    - We updated the 'README.md' file by adding a mission statement that outlines the purpose of the app and our team's
      learning goals.
    - We commented key sections of the 'MainActivity.java' file to explain functionality, such as the API call process, 
      user input handling and UI updates.
    - We worked withing IntelliJ to edit and organize the project structure, ensuring that key files like 'README.md'
      were accessible and properly formatted using Markdown.
    - We explored syncing the local IntelliJ project with GitHub, but held off on pushing changes until all updates were
      finalized since we were having problems snycing with GitHub.
    - We contributed to "build.gradle (:app)" file by updating dependencies, JSON, Android Instrument Tesing using 
      ChatGPT.
    - We privatized our API key in local.properties so it does not sync with GitHub.
    - We used ChatGPT in order for the app to obtain internet access (under AndroidManifest.xml - Line 4).
    - ChatGPT was also used in order to understand what versions of Android Studio, IntelliJ and GitHub were needed for
      our app to work.
    - ApiClient.java was created in order for the App to obtain the weather data requested using Retrofit.
    - We created the WeatherResponse.java class that is a data model that maps the JSON response from the OpenWeatherMap
      API into Java objects using Gson.
    - Since this project was written in Java, but using Android Studio we used ChatGPT to understand how to debug using 
      Toast and Logcat...for example, found in 'MainActivity.java'.  Java's println statements are not used in Android.
      Our powerpoint presentation has the explaination on how we tested and debugged our app.


## In Progress / Planned Changes

    - Write code to obtain a 5-Day forecast and Sever weather alerts.
    - We plan to sync all local changes with out GigHUb repositroy once development stablizes.
    - We considered adding additional UI features like a 5-Day forecast but ran out of time.