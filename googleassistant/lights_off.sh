#!/usr/bin/env bash
set -e
source /home/pi/Documents/Projects/DesktopController/env/bin/activate

printf 'turn off the lights' | python -m googlesamples.assistant.grpc.textinput --device-id 'homedeskcontroller' --device-model-id 'homedeskcontroller-rpi-2b-r6'

deactivate



