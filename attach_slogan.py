import re

# Read the file
with open(r'c:\Users\User\Documents\NetBeansProjects\TechnicalKnowledgeChat\src\com\techchat\view\WelcomeView.fxml', 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Change VBox spacing from 5.0 to 0 (no spacing between elements)
content = content.replace(
    '<VBox alignment="CENTER" spacing="5.0" style="-fx-background-color: transparent;">',
    '<VBox alignment="CENTER" spacing="0" style="-fx-background-color: transparent;">'
)

# 2. Add negative top margin to slogan label to pull it up closer to the brand name
# Find the slogan label and add translateY property
content = re.sub(
    r'(<Label fx:id="sloganLabel" text="Ask, Solve, Done")',
    r'\1 translateY="-20"',
    content
)

# Write back
with open(r'c:\Users\User\Documents\NetBeansProjects\TechnicalKnowledgeChat\src\com\techchat\view\WelcomeView.fxml', 'w', encoding='utf-8') as f:
    f.write(content)

print("Slogan attached directly under brand name!")
