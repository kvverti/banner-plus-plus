# banner-plus-plus
Library mod for the Fabric platform that allows mods to add banner patterns in a hastle-free way. 

## Dependents
To depend on Banner++, add the following to your `build.gradle` repository block.

```groovy
repositories {
	maven {
		url 'https://server.bbkr.space/artifactory/libs-release/'
	}
}
```

## Adding Custom Patterns
Custom patterns are encapsulated in `LoomPattern` objects, which are registered in a vanilla Minecraft registry. Thus, you can register custom patterns by simply adding a new `LoomPattern` to the registry.

```java
// add a normal pattern
Registry.register(LoomPatterns.REGISTRY, new Identifier("modid", "my_pattern"), new LoomPattern(false));
// add a special pattern
Registry.register(LoomPatterns.REGISTRY, new Identifier("modid", "my_pattern"), new LoomPattern(true));
```
Normal patterns are those that don't need an item to be placed in the loom in order to select them, while special patterns do need such an item.

To mark an Item as a pattern item in the Loom, implement the `LoomPatternProvider` interface on the item.
The `LoomPatternItem` class is a convenience subclass of `Item` that implements this interface.

```java
Registry.register(Registry.ITEM, new Identifier("modid", "my_pattern_item"), new LoomPatternItem(MY_PATTERN, itemSettings));
```

## Resource Keys

By default, Loom pattern textures are stored under `modid:textures/pattern/banner/my_pattern.png` and `modid:textures/pattern/shield/my_pattern.png`.

By default, language keys have the form `bannerpp.pattern.modid.my_pattern`.
