(ns zm.zmachine
  (:require [zm.initialization :as ini]
            [zm.random :as rng]
            [zm.memory :refer :all]))

;; A general pattern of the Z-machine is that functions take a state as the
;; first parameter.
;; Functions that need to return a value and a modified state do so as a
;; [state, value] pair.
;; These are just conventions, but it helps a lot.
(def max-memory-by-version
  (let [lo  (* 128 1024)
        med (* 256 1024)
        hi  (* 512 1024)]
    {1 lo  2 lo  3 lo
     4 med 5 med
     6 hi  7 hi  8 hi}))

(defn read-file [story-file]
  "Prepares a new Z-machine state with the story file loaded.
  Story-file is a byte array."
  (let [v  (first story-file)
        zm {:version v
            :rom story-file   ; Immutable ROM of the story file.
            :mem {}           ; Overlaid map of changed bytes.
            :frame nil        ; Topmost frame in the stack.
            :rng (rng/mkrandom) ; RNG seeded unpredictably.
            :output-streams #{1} ; Set of currently selected output streams.
            :stream-3 nil        ; Data structure for the nestable stream.
            :state :running   ; One of :running :read :read-char :quit
                              ; The wrapping re-frame app reads these.
            }]
    (-> zm
        ini/init)))

