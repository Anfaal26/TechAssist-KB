#!/usr/bin/env python3
# Generate dense Circle elements for Möbius waves

colors = [('#58a6ff', '0.6'), ('#1f6feb', '0.5'), ('#79c0ff', '0.4')]
waves = ['mobiusWave1', 'mobiusWave2', 'mobiusWave3']

for wave_num, ((color, opacity), wave_name) in enumerate(zip(colors, waves), 1):
    print(f"\n<!-- Wave {wave_num}: {wave_name} - 250 dots -->")
    circles = []
    for i in range(250):
        circles.append(f'<Circle radius="1.95" opacity="{opacity}" fill="{color}"/>')
        if (i + 1) % 10 == 0:
            print('               ' + ''.join(circles))
            circles = []
    if circles:
        print('               ' + ''.join(circles))
