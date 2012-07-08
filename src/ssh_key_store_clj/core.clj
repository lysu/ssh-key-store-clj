(ns ssh-key-store-clj.core
  (:use [clojure.java.io :only (file)])
  (:require [ssh-key-store-clj.fs :as fs]
            [ssh-key-store-clj.constant :as constant]))

(defn- private-to-pub
  "map private key filename to pub filename"
  [filename]
  (str filename ".pub"))

(defn- gen-store-name
  "store name filename + key"
  [key path]
  (let [origin-file (file path)]
    (str constant/key-store-dir "/" (.getName origin-file) "_" (name key))))

;; hold configuration mem edition.
(def cfg (atom (fs/init-key-store constant/key-store-dir)))

(defn active-key-file 
  "active key file"
  [filename]
  (fs/copy-key-file filename constant/rsa_id-file)
  (fs/copy-key-file (private-to-pub filename) constant/rsa_id_pub-file))

(defn collect-new-file
  "collect new key files"
  [file-info key path]
  (fs/copy-key-file path (gen-store-name key path))
  (fs/copy-key-file (private-to-pub path) (private-to-pub (gen-store-name key path)))
  (let [new-config (fs/add-new-key file-info key (gen-store-name key path))]
    (fs/sync-index-store new-config)
    (reset! cfg new-config)))