import re

# Read the file
with open(r'c:\Users\User\Documents\NetBeansProjects\TechnicalKnowledgeChat\src\com\techchat\view\WelcomeView.fxml', 'r', encoding='utf-8') as f:
    content = f.read()

# Find the slogan section and replace the gradient with solid white using proper FXML syntax
parts = content.split('<!-- Slogan with same metallic effect and glow -->')
if len(parts) == 2:
    before_slogan = parts[0]
    after_slogan_start = parts[1]
    
    # Split at the next comment to isolate slogan section
    slogan_and_rest = after_slogan_start.split('<!-- Simple text links', 1)
    slogan_section = slogan_and_rest[0]
    rest = slogan_and_rest[1] if len(slogan_and_rest) > 1 else ''
    
    # Replace the entire textFill block with proper solid color syntax
    slogan_section = re.sub(
        r'<textFill>.*?</textFill>',
        '<textFill>\n                   <Color fx:constant="WHITE" />\n                </textFill>',
        slogan_section,
        flags=re.DOTALL
    )
    
    # Reconstruct
    content = before_slogan + '<!-- Slogan with same metallic effect and glow -->' + slogan_section + '<!-- Simple text links' + rest

# Write back
with open(r'c:\Users\User\Documents\NetBeansProjects\TechnicalKnowledgeChat\src\com\techchat\view\WelcomeView.fxml', 'w', encoding='utf-8') as f:
    f.write(content)

print("Fixed: Slogan changed to solid white with proper FXML syntax!")
