(ns ssh-key-store-clj.view
  (:use [seesaw.core]
        [seesaw.chooser]))

(defn- display-in
  "render content in frame"
  [frame content]
  (config! frame :content content)
  content)

(native!)

(def f (frame :title "SSH Private Key Manager"
              :on-close :exit
              :width 900
              :height 900))

(def lb-keys (listbox :model []))

(def btn-active (button :text "active"))
(def btn-add (button :text "add"))
(def btn-choose (button :text "choose file"))

(def tlb-choosed (label "please choose private key."))

(def txt-key (text :columns 15))

(display-in f (border-panel
               :center (scrollable lb-keys)
               :north (horizontal-panel
                       :items [(label "key:") txt-key
                               btn-choose tlb-choosed btn-add])
               :south (horizontal-panel
                       :items [btn-active])))