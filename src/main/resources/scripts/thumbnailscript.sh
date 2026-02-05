cd /var/lib/minidlna/Desenhos/Donald_Duck
echo $(java CommandGenerator "$(find -name *.avi)\n$(find -name *.mp4)\n$(find -name *.mkv)")
