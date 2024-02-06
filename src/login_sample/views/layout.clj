(ns login-sample.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to login-sample"]
     (include-css "/css/screen.css")
     (include-js  "/js/htmx.min.js" )
     ]
    [:body body]
   
   ))
