rfjc.jar: distr/RefalJava.zip
	unzip -o $< && touch $@ 
distr/RefalJava.zip:
	wget -O $@ -c http://refal.net/~arklimov/refal6/RefalJava.zip
