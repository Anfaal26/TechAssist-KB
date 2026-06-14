import re

# Read the file
with open(r'c:\Users\User\Documents\NetBeansProjects\TechnicalKnowledgeChat\src\com\techchat\view\WelcomeView.fxml', 'r', encoding='utf-8') as f:
    content = f.read()

# Define the slogan label to insert
slogan_label = '''             
             <!-- Slogan with same metallic effect and glow -->
             <Label fx:id="sloganLabel" text="Ask, Solve, Done" 
                    style="-fx-font-family: 'Ethnocentric', 'Orbitron', 'Bahnschrift', 'Century Gothic', sans-serif; -fx-font-size: 28px; -fx-font-weight: bold; -fx-font-style: italic; -fx-background-color: transparent; -fx-letter-spacing: 0.05em;">
                <textFill>
                   <LinearGradient startX="0" startY="0" endX="0" endY="1">
                      <stops>
                         <Stop offset="0.0" color="#ffffff"/>
                         <Stop offset="0.3" color="#e0e0e0"/>
                         <Stop offset="0.5" color="#c0c0c0"/>
                         <Stop offset="0.7" color="#a8a8a8"/>
                         <Stop offset="1.0" color="#888888"/>
                      </stops>
                   </LinearGradient>
                </textFill>
                <effect>
                   <Blend mode="MULTIPLY">
                      <topInput>
                         <DropShadow color="#1f6feb" radius="15" spread="0.4" blurType="GAUSSIAN"/>
                      </topInput>
                      <bottomInput>
                         <InnerShadow color="#000000" radius="3" choke="0.5" offsetY="-2"/>
                      </bottomInput>
                   </Blend>
                </effect>
             </Label>
'''

# Find the position to insert (after the titleLabel closing tag and before the login links comment)
# Look for pattern: </Label> followed by whitespace and <!-- Simple text links
pattern = r'(</Label>\s+)(<!-- Simple text links)'
replacement = r'\1' + slogan_label + r'\2'

# Replace
new_content = re.sub(pattern, replacement, content, count=1)

# Write back
with open(r'c:\Users\User\Documents\NetBeansProjects\TechnicalKnowledgeChat\src\com\techchat\view\WelcomeView.fxml', 'w', encoding='utf-8') as f:
    f.write(new_content)

print("Slogan label inserted successfully!")
