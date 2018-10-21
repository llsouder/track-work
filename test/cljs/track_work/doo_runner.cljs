(ns track-work.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [track-work.core-test]))

(doo-tests 'track-work.core-test)

