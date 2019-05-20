# Popular Movies: Stage 1  
***developed as part of the Android Developer Nanodegree***

## Building the project
To prevent exposure of the The Movie DB api key I followed 
[this article on Medium](https://medium.com/code-better/hiding-api-keys-from-your-android-repository-b23f5598b906) to keep it inside of my personal gradle properties. You'll need to add a graddle property named `TheMovieDb_v3auth_ApiKey` with a The Movie DB api key (it should also be valid if you want to be able to run the project).

Basically, you should have something like this inside your `/.gradle/gradle.properties` file:
```
TheMovieDb_v3auth_ApiKey="1apikey1apikey1apikey1apikey"
```
