(ns login-sample.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [login-sample.routes.home :refer [home-routes]]
            [login-sample.routes.screen1 :refer [screen1-routes]]
            [login-sample.routes.edit :refer [edit-routes]]
            [login-sample.routes.edit2 :refer [edit-routes2]]
            [noir.session :as session]
            [ring.middleware.session.memory :refer [memory-store]]))

(defn init []
  (println "login-sample is starting"))

(defn destroy []
  (println "login-sample is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes screen1-routes edit-routes edit-routes2 app-routes)
      (handler/site)
      (session/wrap-noir-session
       {:store (memory-store)})))
