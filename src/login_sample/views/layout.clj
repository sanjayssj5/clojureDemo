(ns login-sample.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to login-sample"]
     (include-css "/css/screen.css")
     ]
    [:body body]
   
   ))
