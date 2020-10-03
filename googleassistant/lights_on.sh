#!/bin/bash
printf 'turn the bedroom bill lights on' | python -m googlesamples.assistant.grpc.textinput --device-id 'homedeskcontroller' --device-model-id 'homedeskcontroller-rpi-2b-r6'

