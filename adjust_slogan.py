import re

# Read the file
with open(r'c:\Users\User\Documents\NetBeansProjects\TechnicalKnowledgeChat\src\com\techchat\view\WelcomeView.fxml', 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Change VBox spacing from 20.0 to 5.0 (position slogan closer to brand name)
content = content.replace(
    '<VBox alignment="CENTER" spacing="20.0" style="-fx-background-color: transparent;">',
    '<VBox alignment="CENTER" spacing="5.0" style="-fx-background-color: transparent;">'
)

# 2. Lighten the slogan gradient colors (only in the slogan section, not the title)
# Find the slogan label section and replace its gradient
parts = content.split('<!-- Slogan with same metallic effect and glow -->')
if len(parts) == 2:
    before_slogan = parts[0]
    after_slogan_start = parts[1]
    
    # Split at the next comment to isolate slogan section
    slogan_and_rest = after_slogan_start.split('<!-- Simple text links', 1)
    slogan_section = slogan_and_rest[0]
    rest = slogan_and_rest[1] if len(slogan_and_rest) > 1 else ''
    
    # Replace gradient colors in slogan section only (lighter tones)
    slogan_section = slogan_section.replace('<Stop offset="0.3" color="#e0e0e0"/>', '<Stop offset="0.3" color="#f0f0f0"/>')
    slogan_section = slogan_section.replace('<Stop offset="0.5" color="#c0c0c0"/>', '<Stop offset="0.5" color="#e0e0e0"/>')
    slogan_section = slogan_section.replace('<Stop offset="0.7" color="#a8a8a8"/>', '<Stop offset="0.7" color="#d0d0d0"/>')
    slogan_section = slogan_section.replace('<Stop offset="1.0" color="#888888"/>', '<Stop offset="1.0" color="#c0c0c0"/>')
    
    # 3. Lighten the glow color for slogan (lighter blue)
    slogan_section = slogan_section.replace(
        '<DropShadow color="#1f6feb" radius="15" spread="0.4" blurType="GAUSSIAN"/>',
        '<DropShadow color="#58a6ff" radius="15" spread="0.3" blurType="GAUSSIAN"/>'
    )
    
    # Reconstruct
    content = before_slogan + '<!-- Slogan with same metallic effect and glow -->' + slogan_section + '<!-- Simple text links' + rest

# Write back
with open(r'c:\Users\User\Documents\NetBeansProjects\TechnicalKnowledgeChat\src\com\techchat\view\WelcomeView.fxml', 'w', encoding='utf-8') as f:
    f.write(content)

print("Slogan updated successfully!")
