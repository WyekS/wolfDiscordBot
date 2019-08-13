#!/usr/bin/python3
# coding=utf-8

# This script is based on a small project of the user marceldev89.
# In this continuation it improves with a massive update of mods, the possibility of having several
# instances (having a master) and other minor corrections. It was tested on Ubuntu 19.04


# MIT License
#
# Copyright (c) 2017 Marcel de Vries
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

import os
import os.path
import re
import shutil
import time
import sys

from datetime import datetime
from urllib import request

# Parámetros de entrada (1-actualiza todo, 2-actualiza instancias, 3-actualiza mods)
argument = sys.argv[1]

# Configuración de SteamCMD
STEAM_CMD_MASTER = "/home/arma3hc/steamcmd/steamcmd.sh"
STEAM_CMD_SLAVES = ["/home/arma302/steamcmd/steamcmd.sh", "/home/arma320/steamcmd/steamcmd.sh", "/home/arma330/steamcmd/steamcmd.sh", "/home/arma340/steamcmd/steamcmd.sh"]
STEAM_USER = "<STEAM_USER>"
STEAM_PASS = "<STEAM_PASS>"
A3_SERVER_ID = "233780"

# Grupo de usuario del sistema para la creación de enlaces simbólicos
A3_GROUP_USER = "arma3server"

# Configuración del servidor maestro
A3_SERVER_MASTER = "arma3hc"
A3_SERVER_DIR_MASTER = "/home/arma3hc/serverfiles"

# Configuración de servidores esclavos
A3_SERVER_DIR_SLAVES = {"arma302": "/home/arma302/serverfiles",
                        "arma320": "/home/arma320/serverfiles",
                        "arma330": "/home/arma330/serverfiles",
                        "arma340": "/home/arma340/serverfiles"}

# Configuración para la WORKSHOP
A3_WORKSHOP_ID = "107410"
A3_WORKSHOP_DIR = "{}/steamapps/workshop/content/{}".format(A3_SERVER_DIR_MASTER, A3_WORKSHOP_ID)
PATTERN_MOD = "@"
TO_EXCLUDE = "&"
PATTERN = re.compile(r"workshopAnnouncement.*?<p id=\"(\d+)\">", re.DOTALL)
WORKSHOP_CHANGELOG_URL = "https://steamcommunity.com/sharedfiles/filedetails/changelog"

# Lista de MODS. TODO hacer que lea un fichero o una peticion json/html
MODS = {
    "3den_better_inventory": "1124993203",
    "3den_enhanced": "623475643",
    "ace": "463939057",
    "ace_compat_-_rhs_armed_forces_of_the_russian_federation": "773131200",
    "ace_compat_-_rhs_united_states_armed_forces": "773125288",
    "acex": "708250744",
    "achilles": "723217262",
    "acre2": "751965892",
    "ADV - ACE Medical": "1353873848",
    "advanced_civilian_interaction_module": "828453337",
    "advanced_towing": "639837898",
    "advanced_urban_rappelling": "730310357",
    "alive": "620260972",
    "asr_ai3": "642457233",
    "asr_ai3_-_project_opfor_config": "849435425",
    "backpackonchest": "820924072",
    "blastcore_edited_(standalone_version)": "767380317",
    "cba_a3": "450814997",
    "enhanced_movement": "333310405",
    "gren_evo": "1336178086",
    "jbad": "520618345",
    "kunduz_afghanistan_-_doors_&_multiplayer_fix": "1623903743",
    "mlo": "823636749",
    "multi-play_uniforms": "779520435",
    "project_opfor": "735566597",
    "rhsafrf": "843425103",
    "rhsusaf": "843577117",
    "sma_rhs_compatibility_patch": "1112431110",
    "specialist_military_arms_(sma)_version_2.7.1": "699630614",
    "tac_vests": "779568775",
    "tryk_multiplay-uniform_fix": "741196544",
    "lythium": "909547724",
    "shacktac_user_interface": "498740884",
    "CLA CLAFGHAN":"761349672",
    "CUP Terrains - Core":"583496184",
    "Project Human 1.2":"1188797395",
    "Spyder Addons":"579263829",
    "CUP Terrains - Maps":"583544987",
    "G.O.S Al Rayak":"648172507",
    "Jbad":"520618345",
    "athena":"1181881736",
    "Advanced Urban Zipline":"1576381952",
    "Ace Medical Assistant":"1677267612",
    "Advanced Civilian Interaction Module":"828453337",
    "DES Elevator":"708665067",
    "Distrikt 41 - Ruegen":"835257174",
    "DS Houses":"1113351114",
    "Electronic Warfare":"1568688945",
    "Em_Buildings":"671539540",
    "Terrorist Organization Black Order":"654170014",
    "Terrorist Organization Black Order - ACE Compat":"1250708527",
    "Grenade Window Breaker":"1702704179",
    "FIREWILL Aviation Pack":"1381545544"
}

def log(msg):
    print("")
    print("{{0:=<{}}}".format(len(msg)).format(""))
    print(msg)
    print("{{0:=<{}}}".format(len(msg)).format(""))


def call_steamcmd(user, steam_cmd, params):
    '''
    Ejecutará el comando en steamcmd con un usuario determinado

    :user Nombre del usuario de la instancia
    :param steam_cmd La ruta del steamcmd de la instancia a actualizar
    :param params Estos parámetros se pasarán a la consola de Steam
    '''
    command = "su - {} -c \"{} {}\"".format(user, steam_cmd, params)
    # log(command)
    result = os.system(command)
    print("")
    if result != 0:
        sys.exit(-1)

# Create symlink
def create_mod_symlinks(a3_server_dir):
    '''
    Crea enlaces simbólicos en la ruta indicada por parámetro

    :a3_server_dir Ruta donde creará los enlaces simbólicos
    '''
    mod_list=""
    for mod_name, mod_id in MODS.items():
        mod_name=cleanModName(mod_name)
        link_path="{}/{}{}".format(a3_server_dir, PATTERN_MOD, mod_name)
        real_path="{}/{}".format(A3_WORKSHOP_DIR, mod_id)

        # mod_list info all mods to script sh
        mod_list += PATTERN_MOD + mod_name + "\;"

        if os.path.isdir(real_path):
            if not os.path.islink(link_path):
                os.symlink(real_path, link_path)
                print("Creating symlink '{}'...".format(link_path))
        else:
            print("Mod '{}' does not exist! ({})".format(mod_name, real_path))

    return mod_list


# Update server
def update_server(user, steamcmd_path, a3_server_dir):
    '''
    Actualizará el mod según el id que llega por parámetro

    :user Nombre del usuario de la instancia
    :param steam_cmd La ruta del steamcmd de la instancia a actualizar
    :param a3_server_dir Ruta de la instancia de arma para construir los parámetros
    '''
    steam_cmd_params = " +login {} {}".format(STEAM_USER, STEAM_PASS)
    steam_cmd_params += " +force_install_dir {}".format(a3_server_dir)
    steam_cmd_params += " +app_update {} validate".format(A3_SERVER_ID)
    steam_cmd_params += " +quit"

    call_steamcmd(user, steamcmd_path, steam_cmd_params)


def mod_needs_update(mod_id, path):
    '''
    Comprueba si el mod necesita ser actualizado

    :mod_id ID del mod a comprobar
    :path Ruta del mod en el sistema
    '''
    if os.path.isdir(path):
        response = request.urlopen(
            "{}/{}".format(WORKSHOP_CHANGELOG_URL, mod_id)).read()
        response = response.decode("utf-8")
        match = PATTERN.search(response)

        if match:
            updated_at = datetime.fromtimestamp(int(match.group(1)))
            created_at = datetime.fromtimestamp(os.path.getctime(path))

            return (updated_at >= created_at)

    return False


def update_mods():
    '''
    Este método solo será llamado una vez, tiene que haber una instancia maestra para la actualización de mods
    En nuestro caso el usuario maestro es "arma3hc"

    '''
    steam_cmd_params = "+login {} {}".format(STEAM_USER, STEAM_PASS)
    steam_cmd_params += " +force_install_dir {}".format(A3_SERVER_DIR_MASTER)

    for mod_name, mod_id in MODS.items():
        path = "{}/{}".format(A3_WORKSHOP_DIR, mod_id)

        # Check if mod needs to be updated
        if os.path.isdir(path):

            if mod_needs_update(mod_id, path):
                # Delete existing folder so that we can verify whether the
                # download succeeded
                shutil.rmtree(path)
            else:
                print("No update required for \"{}\" ({})... SKIPPING".format(
                    mod_name, mod_id))
                continue

        # Keep trying until the download actually succeeded
        steam_cmd_params += " +workshop_download_item {} {} validate".format(
            A3_WORKSHOP_ID,
            mod_id
        )
    # log("Mods to update: {}".format(steam_cmd_params))
    steam_cmd_params += " +quit"

    # Llamamos a la consola con el usuario maestro y la lista de mods a actualizar/descargar
    call_steamcmd(A3_SERVER_MASTER, STEAM_CMD_MASTER, steam_cmd_params)

# No se está usando este método, se ha comprobar y las instancias funcionan sin cambiar permisos
def update_permissions_mods():
    os.system("chmod -R 2777 {}/{}* ".format(A3_WORKSHOP_DIR, PATTERN_MOD))


def update_permissions_links(user, a3_server_dir):
    '''
    Actualizará los permisos de los enlaces simbólicos creados en una ruta determinada

    :param user Usuario propetario del enlace
    :param a3_server_dir Directorio de la instancia
    '''
    # For user specific
    os.system("chown -R {} {}/{}* ".format(user, a3_server_dir, PATTERN_MOD))
    # For user group
    os.system("chgrp -R {} {}/{}* ".format(A3_GROUP_USER, a3_server_dir, PATTERN_MOD))
    # For permissions files
    os.system("chmod -R 2777 {}/{}* ".format(a3_server_dir, PATTERN_MOD))


def lowercase_workshop_dir():
    '''
    Convierte a minúsculas las carpetas y ficheros de todos los mods
    '''
    # os.system("(cd {} && find . -depth -exec rename -v 's/(.*)\/([^\/]*)/$1\/\L$2/' {{}} \;)".format(A3_WORKSHOP_DIR))
    # os.system("su - {} -c \"(cd {} && find . -depth -exec rename -v 's/(.*)\/([^\/]*)/$1\/\L$2/' {{}} \;)\"".format(
    #    A3_SERVER_MASTER, A3_WORKSHOP_DIR))
    os.system("su - {} -c \"cd {} && ./min.sh\"".format(A3_SERVER_MASTER, A3_WORKSHOP_DIR))


def cleanModName(modName):
    '''
    Limpia los nombres de los mods para el enlace simbólico(& se excluye y espacio se cambia por _)
    '''
    modName = modName.replace(TO_EXCLUDE, "")
    modName = modName.replace(" ", "_")
    modName = modName.lower()
    return modName


#############################
#      FLUJO DEL SCRIPT     #
#############################

# Actualizar el server maestro
if argument == "1" or argument == "2":
    log("Updating MASTER A3 server ({})".format(A3_SERVER_ID))
    update_server(A3_SERVER_MASTER, STEAM_CMD_MASTER, A3_SERVER_DIR_MASTER)

# Iterar las instancias de arma y actualizarlas
if argument == "1" or argument == "2":
    i=0
    for a3_instance, a3_server_dir in A3_SERVER_DIR_SLAVES.items():
        log("Updating SLAVE {} A3 server ({})".format(i, a3_instance))
        update_server(a3_instance, STEAM_CMD_SLAVES[i], a3_server_dir)
        i += 1

if argument == "2":
    sys.exit(0)

# Actualiza los mods en la instancia maestra, así no los tendremos duplicados
if argument == "1" or argument == "3":
    log("Updating mods to MASTER")
    update_mods()
    print("")

    # Una sola llamada para convertir a minusculas
    log("Converting uppercase files/folders to lowercase...")
    lowercase_workshop_dir()
    print("")

    # Enlaces simbólicos para el MASTER
    log("Symlink to MASTER {}".format(A3_SERVER_DIR_MASTER.upper()))
    mod_list = create_mod_symlinks(A3_SERVER_DIR_MASTER)
    print("Updating permissions of symlinks created for \"{}\"".format(A3_SERVER_DIR_MASTER))
    update_permissions_links(A3_SERVER_MASTER, A3_SERVER_DIR_MASTER)
    print("")


# Enlaces simbólicos para los ESCLAVOS
    for a3_instance, a3_server_dir in A3_SERVER_DIR_SLAVES.items():
        log("SYMLINK FOR " + a3_instance.upper())
        create_mod_symlinks(a3_server_dir)
        print("Updating permissions of symlinks created for \"{}\"".format(a3_instance))
        update_permissions_links(a3_instance, a3_server_dir)
        print("")

# log("Copy this line for load mods : {}".format(mod_list))


print(" by Wolf Team ")
sys.exit(0)
