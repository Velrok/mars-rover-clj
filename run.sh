#!/usr/bin/env bash

hash lein   2>/dev/null && hasLein=Y || hasLein=N
hash docker 2>/dev/null && hasDocker=Y || hasDocker=N

# is leiningen installed?
if [ "$hasLein" = "Y" ]
then
  cat inputs | lein run
else
  echo "leiningen not installed."
  echo "fallback to using docker"

  if [ "$hasDocker" = "Y" ]
  then
    docker run --mount type=bind,source="$(pwd)",destination=/tmp clojure:lein-2.9.1 cat inputs | lein run
  else
    echo "You need to have either leiningen or docker installed. :("
    exit 1
  fi
fi
