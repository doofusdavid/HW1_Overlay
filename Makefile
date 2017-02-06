all: compile
@echo -e '[INFO] Done!'

clean:
@echo -e '[INFO] Cleaning Up...'
@rm -rf src/cs455/**/**/*.class

compile:
@echo -e '[INFO] Compiling...'
@javac -d . src/cs455/**/**/*.java
