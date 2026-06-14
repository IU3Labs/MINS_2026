# Usage: ./brainfuck.py [FILE]

import sys

def execute(filename, data=[]):
  f = open(filename, "r")
  result = evaluate(f.read(), data)
  f.close()
  return result


def evaluate(code, data):
  code = cleanup(list(code))
  bracemap = buildBraceMap(code)

  cells, codeptr, cellptr = [0] + list(data[:32]) + [0] * (80 - min(len(data), 32)), 0, 0

  print("Start of evaluate")
  print_cells(cells, cellptr)

  while codeptr < len(code):
    command = code[codeptr]

    if command == ">":
      cellptr += 1
      if cellptr == len(cells): cells.append(0)

    if command == "<":
      cellptr = 0 if cellptr <= 0 else cellptr - 1

    if command == "+":
      cells[cellptr] = cells[cellptr] + 1 if cells[cellptr] < 255 else 0

    if command == "-":
      cells[cellptr] = cells[cellptr] - 1 if cells[cellptr] > 0 else 255

    if command == "[" and cells[cellptr] == 0: codeptr = bracemap[codeptr]
    if command == "]" and cells[cellptr] != 0: codeptr = bracemap[codeptr]
    if command == ".":
      print_cells(cells, cellptr)
      print(chr(cells[cellptr]), end="")
    if command == ",":
      print_cells(cells, cellptr)
      cells[cellptr] = ord(input()[0])

    if cells[0] == 1:
      cells = cells[:49] + downDataCheck(cells) + cells[81:]
      cells[0] = 0
    elif cells[0] == 255:
      print("up")
      print_cells(cells, cellptr)
      return upDataCheck(cells)
    
    codeptr += 1
    # print_cells(cells, cellptr)


def cleanup(code):
  return ''.join(filter(lambda x: x in ['.', ',', '[', ']', '<', '>', '+', '-'], code))


def buildBraceMap(code):
  temp_bracestack, bracemap = [], {}

  for position, command in enumerate(code):
    if command == "[": temp_bracestack.append(position)
    if command == "]":
      start = temp_bracestack.pop()
      bracemap[start] = position
      bracemap[position] = start
  return bracemap


#если в 0-й ячейке - 1
def downDataCheck(cells):
  filename, data = ''.join(c for c in bytes(cells[33:49 + cells[33:49].index(0)]).decode('ascii', errors='ignore') if c.isprintable()) + ".bf", cells[49:81]
  print(f"{filename} down with data: {data}")
  return execute(filename, data)

# если в 0-й ячейке - 255
def upDataCheck(cells):
  return cells[1:33]


def print_cells(cells, ptr):
  """
  Выводит массив cells в одну строку (без ограничения ширины) с подписями индексов снизу.
  Элемент с индексом ptr выделяется красным цветом.
  """
  if not cells:
    return

  # Преобразуем все элементы в строки для определения ширины
  str_cells = [str(c) for c in cells]
  max_len = max(len(s) for s in str_cells)  # ширина колонки

  value_parts = []
  index_parts = []

  for i, val in enumerate(str_cells):
    val_formatted = val.rjust(max_len)
    idx_formatted = str(i).rjust(max_len)

    if i == ptr:
      # ANSI-код красного цвета
      value_parts.append(f"\033[91m{val_formatted}\033[0m")
      index_parts.append(f"\033[91m{idx_formatted}\033[0m")
    else:
      value_parts.append(val_formatted)
      index_parts.append(idx_formatted)

  # Собираем строки с разделителем пробел
  print(" ".join(value_parts))
  print(" ".join(index_parts))
  print()


def main():
  if len(sys.argv) == 2: execute(sys.argv[1])
  else: 
    print("Usage:", sys.argv[0], "filename")
    execute("main.bf")

if __name__ == "__main__": main()

