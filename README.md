# graveler

Minecraft mod that adds structural integrity to the game's building systems.

## Install

Download it from [Modrinth](https://modrinth.com/project/graveler) or [GitHub Releases](https://github.com/skippi/graveler/releases).

## Algorithm

Graveler stores "stress" per chunk. Stress is calculated as following:

```
stress = minNeighborStress + cantileverPenalty - archSupport
```

- `cantileverPenalty`: If the current block is not supported from below, increase stress.
- `archSupport`: If a block is surrounded by two blocks on either horizontal side, there is a compressive force that reduces stress.
