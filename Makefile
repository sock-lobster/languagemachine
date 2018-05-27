all: LMApplication.class LMOptionsPanel.class LMToolbar.class \
	LMWorkspacePanel.class NFA.class Rule.class StartState.class \
	State.class Transition.class LMStateOptionsToolbar.class \
	LMTransitionOptionsToolbar.class LMInputToolbar.class \
	LMMenubar.class LMMessagePanel.class

LMApplication.class: LMApplication.java
	javac LMApplication.java
LMOptionsPanel.class: LMOptionsPanel.java
	javac LMOptionsPanel.java
LMToolbar.class: LMToolbar.java
	javac LMToolbar.java
LMWorkspacePanel.class: LMWorkspacePanel.java
	javac LMWorkspacePanel.java
LMStateOptionsToolbar.class: LMStateOptionsToolbar.java
	javac LMStateOptionsToolbar.java
LMTransitionOptionsToolbar.class: LMTransitionOptionsToolbar.java
	javac LMTransitionOptionsToolbar.java
LMInputToolbar.class: LMInputToolbar.java
	javac LMInputToolbar.java
LMMenubar.class: LMMenubar.java
	javac LMMenubar.java
LMMessagePanel.class: LMMessagePanel.java
	javac LMMessagePanel.java
NFA.class: NFA.java 
	javac NFA.java
Rule.class: Rule.java
	javac Rule.java
State.class: State.java
	javac State.java
StartState.class: StartState.java
	javac StartState.java
Transition.class: Transition.java 
	javac Transition.java

clean: 
	$(RM) *.class

run: LMApplication.class LMOptionsPanel.class LMToolbar.class \
	LMWorkspacePanel.class NFA.class Rule.class StartState.class\
	State.class Transition.class LMStateOptionsToolbar.class\
	LMTransitionOptionsToolbar.class LMInputToolbar.class \
	LMMenubar.class LMMessagePanel.class
	java LMApplication