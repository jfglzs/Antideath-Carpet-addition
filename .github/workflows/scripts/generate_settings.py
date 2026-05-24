"""
Generate settings.json for TIS-style CI scripts.
"""

__author__ = "jasonxue"

import json
from pathlib import Path


def version_key(version: str) -> tuple[int, ...]:
    return tuple(int(part) for part in version.split("."))


def main():
    versions = sorted(
        (
            path.name
            for path in Path("versions").iterdir()
            if path.is_dir() and (path / "gradle.properties").is_file()
        ),
        key=version_key,
    )
    with open("settings.json", "w", encoding="utf8") as f:
        json.dump({"versions": versions}, f, indent=2)
        f.write("\n")


if __name__ == "__main__":
    main()
