# Changelog

## [3.0.0](https://github.com/MaksymDolgykh/dropwizard-micrometer/compare/v2.0.9...v3.0.0) (2022-12-14)


### ⚠ BREAKING CHANGES

* application Configuration class has to implement MicrometerBundleConfiguration

### Features

* make bundle configurable ([cbb1f93](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/cbb1f93f39a3edc3724d25af23093f0715215440)), closes [#19](https://github.com/MaksymDolgykh/dropwizard-micrometer/issues/19)


### Documentation

* update doc to include configuration step required to setup bundle ([21380c0](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/21380c04e518263d2c3918d7c84b47ce3971ccad))


### Miscellaneous Chores

* **master:** release 2.0.10-SNAPSHOT ([ae931d6](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/ae931d65d3ec110b2bda47f6c2c023cf57d26ae4))

## [2.0.9](https://github.com/MaksymDolgykh/dropwizard-micrometer/compare/v2.0.8...v2.0.9) (2022-11-28)


### Miscellaneous Chores

* additional micrometer binders ([34adce4](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/34adce428d1430769a8314f4a31e1ce17dd367a7))
* **master:** release 2.0.9-SNAPSHOT ([09859ce](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/09859ce1b5ac8bf76b52b6e0b3edeadd76c4eb0d))
* upgrade core dependencies for security patching (dropwizard, prometheus, micrometer) ([c25d74b](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/c25d74bd5ea481d3bbcf61210d1d5d5872f76454))

## [2.0.8](https://github.com/MaksymDolgykh/dropwizard-micrometer/compare/v2.0.7...v2.0.8) (2022-11-11)


### Miscellaneous Chores

* **master:** release 2.0.8-SNAPSHOT ([2927ddc](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/2927ddc7e045f7a0e8d5b2b20d2d779f292e12bc))

## [2.0.7](https://github.com/MaksymDolgykh/dropwizard-micrometer/compare/v2.0.6...v2.0.7) (2022-11-11)


### Miscellaneous Chores

* **master:** release 2.0.7-SNAPSHOT ([4168490](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/41684903490465dad831f77895a7683001de9cf4))

## [2.0.6](https://github.com/MaksymDolgykh/dropwizard-micrometer/compare/v2.0.5...v2.0.6) (2022-11-11)


### Continuous Integration

* add release workflow ([8d4a19d](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/8d4a19db0de8fb44f3af8ff71609ea6d7dc14f5f))
* add test-pr workflow ([5d49eda](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/5d49edaec145e554ddc50e08b280bee33e14fe7b))


### Documentation

* update docs ([1b8b1f0](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/1b8b1f0c0a6de3817c3560d70384b2f8931a8fb7))


### Miscellaneous Chores

* **master:** release 2.0.6-SNAPSHOT ([ee64a7b](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/ee64a7b40a3a246f5b54f2050e7b2236b0a574dc))
* release 2.0.6 ([89a5230](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/89a52301b75a78c750291feddc727cf6c8fe4223))

## [2.0.5](https://github.com/MaksymDolgykh/dropwizard-micrometer/compare/v1.0.0...v2.0.5) (2021-01-31)


### Features

* implement JDBI latency metrics and split to 2 packages (core and jdbi) ([bf2ce41](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/bf2ce4102ce3bbebafea4a22835c7f508ac500d3))

### Documentation

* update docs to reflect 2.0.x changes ([e5fd57a](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/e5fd57a86e7ccda99c9403827d55eff8460609fe))


## 1.0.0 (2020-09-30)


### Features

* add MicrometerBundle and servlet filter ([da224fd](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/da224fd3b5aecbcbfe9531c18ea4ec91bcf4cd3c))
* add integration with OSSRH ([1fe4541](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/1fe45413316dee2e6a074d0ffe0ba3467ab16efb))

### Tests

* add tests ([4260f9c](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/4260f9c8cd0726435ccc9a20e80341b45934eef5))

### Documentation

* add usage doc into readme ([f4c7751](https://github.com/MaksymDolgykh/dropwizard-micrometer/commit/f4c77513e628415a41c3a022629a3128df0de0c5))
