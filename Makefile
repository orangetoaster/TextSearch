JUNIT = junit.jar
CLASSPATH = -cp $$CLASSPATH:$(OBJ_DIR):$(JUNIT)
JAVA = java $(CLASSPATH)
JAVAC = javac $(CLASSPATH) -Xlint
OBJ_DIR = Build/
SRCS = $(wildcard DocumentCompare/*.java)
TESTSRC = $(wildcard TestDocumentCompare/*.java)
ALLSRC = $(SRCS) $(TESTSRC)

all: $(addprefix $(OBJ_DIR), $(ALLSRC:java=class))

run: $(addprefix $(OBJ_DIR), $(SRCS:java=class))
	java -cp Build DocumentCompare.DocCompare -q query/10.txt

test: all
	$(JAVA) org.junit.runner.JUnitCore TestDocumentCompare.TestMultiSet TestDocumentCompare.TestTerm 

clean: 
	rm -Rf $(OBJ_DIR)  Docs

docs:
	doxygen Doxyfile

$(OBJ_DIR)%.class: %.java
	@mkdir -p $(@D)
	$(JAVAC) $< -d $(OBJ_DIR)
