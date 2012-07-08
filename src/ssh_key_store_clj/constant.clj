(ns ssh-key-store-clj.constant
  (:import java.lang.System))

(def ^:const home-path
  (java.lang.System/getenv "HOME"))

(def ^:const key-store-dir
  (str home-path "/.ssh/bk"))

(def ^:const rsa_id-file
  (str home-path "/.ssh/id_rsa"))

(def ^:const rsa_id_pub-file
  (str home-path "/.ssh/id_rsa.pub"))

(def ^:const sys-separator
  (java.lang.System/getProperty "line.separator"))