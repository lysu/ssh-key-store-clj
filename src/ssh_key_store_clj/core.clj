(ns ssh-key-store-clj.core
  (:use [seesaw.core]
        [seesaw.chooser]
        [clojure.java.io :only (file)]
        [clojure.string :only (join)])
  (:require [ssh-key-store-clj.fs :as fs])
  (:import java.lang.System))

(def ^:const home-path
  (str (java.lang.System/getenv "HOME")))

(def ^:const key-store-dir
  (str home-path "/.ssh/bk"))

(def ^:const rsa_id-file
  (str home-path "/.ssh/id_rsa"))

(def ^:const rsa_id_pub-file
  (str home-path "/.ssh/id_rsa.pub"))

(defn- private-to-pub
  "map private key filename to pub filename"
  [filename]
  (str filename ".pub"))

(defn- display-in
  "render content in frame"
  [frame content]
  (config! frame :content content)
  content)

(defn- gen-store-name
  "store name filename + key"
  [key path]
  (let [origin-file (file path)]
    (str key-store-dir "/" (.getName origin-file) "_" (name key))))

;; hold configuration mem edition.
(def cfg (atom (fs/init-key-store key-store-dir)))

(defn- active-key-file 
  "active key file"
  [filename]
  (fs/copy-key-file filename rsa_id-file)
  (fs/copy-key-file (private-to-pub filename) rsa_id_pub-file))

(defn- collect-new-file
  "collect new key files"
  [file-info key path]
  (fs/copy-key-file path (gen-store-name key path))
  (fs/copy-key-file (private-to-pub path) (private-to-pub (gen-store-name key path)))
  (let [new-config (fs/add-new-key file-info key (gen-store-name key path))]
    (fs/sync-index-store new-config)
    (reset! cfg new-config)))

(defn -main 
  "main entry point for invoke seesaw"
  [& arg]
  (let [f (frame :title "SSH Private Key Manager"
                 :on-close :exit
                 :width 600
                 :height 500)]
    (invoke-later
     (native!)
     (-> f pack! show!)
     (let [lb-keys (listbox :model
                            (map name (keys (:key-files @cfg))))
           btn-active (button :text "active")
           btn-add (button :text "add")
           btn-choose (button :text "choose file")
           tlb-choosed (label "please choose private key.")
           txt-key (text)]
       (display-in f (border-panel
                      :center (scrollable lb-keys)
                      :north (horizontal-panel
                              :items [(label "key:") txt-key
                                      btn-choose tlb-choosed btn-add])
                      :south (horizontal-panel
                              :items [btn-active])))
       (listen btn-active :action
               (fn [e]
                 (let [selected-key (keyword (selection lb-keys))
                       selected-filename (-> @cfg :key-files selected-key :path)]               
                   (active-key-file selected-filename)
                   (alert e "actived!!"))))
       (listen btn-choose :action
               (fn [e] (choose-file f :success-fn
                                    (fn [fc file]
                                      (config! tlb-choosed :text (.getAbsolutePath file))))))
       (listen btn-add :action
               (fn [e]
                 (collect-new-file @cfg (keyword (text txt-key)) (text tlb-choosed))
                 (config! lb-keys :model (map name
                                              (keys (:key-files @cfg))))
                 (alert e "add!!")))))))






