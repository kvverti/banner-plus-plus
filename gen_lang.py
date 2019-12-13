#!/usr/bin/python3
"""
A script that generates banner pattern language file entries for each color
based on a base language key and value.
"""
import sys

colors = {
    'black': 'Black',
    'red': 'Red',
    'green': 'Green',
    'brown': 'Brown',
    'blue': 'Blue',
    'purple': 'Purple',
    'cyan': 'Cyan',
    'light_gray': 'Light Gray',
    'gray': 'Gray',
    'pink': 'Pink',
    'lime': 'Lime',
    'yellow': 'Yellow',
    'light_blue': 'Light Blue',
    'magenta': 'Magenta',
    'orange': 'Orange',
    'white': 'White'
}

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print('Usage: {0} <lang-key-base> <translation-base>'.format(sys.argv[0]))
    else:
        key = sys.argv[1]
        trans = sys.argv[2]
        for id, name in colors.items():
            print('"bannerpp.pattern.bannerpp.{0}.{1}": "{2} {3}",'.format(key, id, name, trans))
