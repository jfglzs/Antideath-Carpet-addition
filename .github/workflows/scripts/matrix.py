"""
A script to scan through the versions directory and collect all folder names as the subproject list,
then output a json as the github action include matrix
"""

# Source: https://github.com/TISUnion/Carpet-TIS-Addition/blob/master/.github/workflows/scripts/matrix.py
#
# This file is part of the Carpet TIS Addition project, licensed under the
# GNU Lesser General Public License v3.0
#
# Copyright (C) 2023  Fallen_Breath and contributors
#
# Carpet TIS Addition is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Carpet TIS Addition is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
__author__ = "Fallen_Breath"

import json
import os
import sys


def main():
    target_subproject_env = os.environ.get("TARGET_SUBPROJECT", "")
    target_subprojects = [x for x in target_subproject_env.split(",") if x]
    print("target_subprojects: {}".format(target_subprojects))

    with open("settings.json") as f:
        settings: dict = json.load(f)

    if len(target_subprojects) == 0:
        subprojects = settings["versions"]
    else:
        subprojects = []
        for subproject in settings["versions"]:
            if subproject in target_subprojects:
                subprojects.append(subproject)
                target_subprojects.remove(subproject)
        if len(target_subprojects) > 0:
            print(
                "Unexpected subprojects: {}".format(target_subprojects), file=sys.stderr
            )
            sys.exit(1)

    matrix_entries = []
    for subproject in subprojects:
        matrix_entries.append(
            {
                "subproject": subproject,
            }
        )
    matrix = {"include": matrix_entries}
    with open(os.environ["GITHUB_OUTPUT"], "w") as f:
        f.write("matrix={}\n".format(json.dumps(matrix)))

    print("matrix:")
    print(json.dumps(matrix, indent=2))


if __name__ == "__main__":
    main()
