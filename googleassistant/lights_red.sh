#!/bin/bash
printf 'set bedroom bill lights to on \n set bedroom bill lights to red \n set bedroom bill lights to 100 percent' | python -m googlesamples.assistant.grpc.textinput --device-id 'homedeskcontroller' --device-model-id 'homedeskcontroller-rpi-2b-r6'




