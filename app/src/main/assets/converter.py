from pathlib import Path
if __name__ == "__main__":

    categories = {}
    # open the 'sokkyo' directory in the same folder as this file
    sokkyo_dir = Path(__file__).parent / 'sokkyo'
    # get all files in the 'sokkyo' directory that end in '.csv'
    csv_files = list(sokkyo_dir.glob('*.csv'))
    # iterate over each file
    file_components = []
    file_components.append("""name = "Sokkyo"

        [categories]""")
    for file in csv_files:
        # get the name of the file without the extension
        category_name = file.stem
        print(category_name)
        file_components.append(f"\n{category_name} = [")
        # open the file
        with open(file, 'r', encoding='utf-8') as f:
            # read the file and split it by lines
            lines = f.read().splitlines()
            # add the lines to the categories dictionary
            categories[category_name] = lines
            for line in lines:
                file_components.append(f'\t"{line}",')
        file_components.append("]")

    # Convert to TOML file
    with open(Path(__file__).parent / 'sokkyo_pack.toml', 'w', encoding='utf-8') as f:
        f.write('\n'.join(file_components))


