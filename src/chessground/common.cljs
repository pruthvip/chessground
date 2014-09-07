(ns chessground.common
  "Shared utilities for the library")

(enable-console-print!)

(def debug true)

(defn pp [& exprs]
  (when debug (doseq [expr exprs] (.log js/console (clj->js expr))))
  (first exprs))

(defn file->pos [key]
  (inc (.indexOf "abcdefgh" (first key))))

(defn key->pos [key]
  [(file->pos (first key)) (js/parseInt (second key))])

(defn pos->key [pos]
  (str (get "abcdefgh" (dec (first pos))) (second pos)))

(defn opposite-color [color] (case color
                               "white" "black"
                               "black" "white"
                               nil))

(defn toggle [hashmap field value]
  (if value
    (assoc hashmap field true)
    (dissoc hashmap field)))

(defn deep-merge [a b]
  (letfn [(smart-merge [x y]
            (if (and (map? x) (map? y))
              (merge x y)
              (or y x)))]
    (merge-with smart-merge a b)))

(defn seq-contains? [coll target] (some #{target} coll))

(defn offset [element]
  (let [rect (.getBoundingClientRect element)]
    {:top (+ (.-top rect) (-> js/document .-body .-scrollTop))
     :left (+ (.-left rect) (-> js/document .-body .-scrollLeft))}))

; mimics the JavaScript `in` operator
(defn js-in? [obj prop]
  (and obj (or (.hasOwnProperty obj prop)
               (js-in? (.-__proto__ obj) prop))))

(defn map-values [f hmap]
  (into {} (for [[k v] hmap] [k (f v)])))

(defn keywordize-keys [hashmap]
  (when hashmap (into {} (for [[k v] hashmap] [(keyword k) v]))))

(defn stringify-keys [hashmap]
  (when hashmap (into {} (for [[k v] hashmap] [(name k) v]))))
