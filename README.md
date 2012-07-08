# SSH-Key-Store

##Introduction

a SSH key manager tool, give a short way to switch between different rsa_id/rsa_id.pub version.

take free from input password/cisco ras everyday and "jump machine problem"... ^ ^

writen in clojure and use seesaw as GUI render

## Installation

SSH-Key-Store bootstraps itself using the `ssh-key-store` shell script; there is no separate install script. It installs its dependencies upon the first run on unix, so the first run will take longer.

* [Download the script.](https://raw.github.com/lysu/ssh-key-store-clj/master/bin/ssh-key-store)
* Place it on your $PATH. (I like to use ~/bin)
* Set it to be executable. (`chmod 755 ~/bin/ssh-key-store`)

The link above will get you the stable release. 

On Windows most users can get the batch file. If you have wget.exe or curl.exe already installed and in PATH, you can just run `ssh-key-store self-install`, otherwise get the standalone jar from the downloads page. If you have Cygwin you should be able to use the shell script above rather than the batch file.


## Usage

Run ssh-key-store by:
    
	ssh-key-store

It will display a simple windows which u can ADD new key  or ACTIVE a existing key..

## License

Copyright © 2012 lysu

Distributed under the Eclipse Public License, the same as Clojure.
