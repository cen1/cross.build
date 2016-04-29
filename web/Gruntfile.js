module.exports = function (grunt) {

    var livereload = require('connect-livereload'),
        modRewrite = require('connect-modrewrite'),
        path = require('path');

    require('load-grunt-tasks')(grunt);

    var paths = {
        dist: '../../../target/' + grunt.option('projectName')
    };

    grunt.initConfig({
        paths: paths,
        connect: {
            options: {
                port: process.env['PORT'] || 3002,
                hostname: '0.0.0.0'
            },
            livereload: {
                options: {
                    middleware: function (connect) {
                        return [
                            modRewrite(['^((?!\\.css|\\.js|\\.html|\\.eot|\\.svg|\\.ttf|\\.woff|\\.png|\\.jpg|\\.pdf|\\.jpeg).)*$ /index.html [L]']),
                            livereload({port: 35730}),
                            connect.static(path.resolve('.tmp')),
                            connect.static(path.resolve('./'))
                        ];
                    }
                }
            }
        },
        watch: {
            options: {
                livereload: 35730
            },
            template: {
                files: [
                    'scripts/*.tpl',
                    'index.html'
                ],
                tasks: ['template:develop']
            },
            files: {
                files: [
                    'images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}'
                ]
            }
        },
        clean: {
            options: {
                force: true
            },
            temp: {
                src: ['.tmp']
            },
            dist: {
                src: ['<%= paths.dist %>']
            }
        },
        useminPrepare: {
            src: ['index.html'],
            options: {
                dest: '<%= paths.dist %>'
            }
        },
        template: {
            options: {
                data: {
                    version: grunt.file.readJSON("package.json").version,
                    api: process.env['APIURL'],
                    root: process.env['ROOT'],
                    keycloak: process.env['KEYCLOAKURL'],
                    keycloak_key: process.env['KEYCLOAKKEY']
                }
            },
            develop: {
                files: {
                    '.tmp/scripts/config.js': ['scripts/config.tpl.js'],
                    '.tmp/keycloak.json': ['keycloak.tpl.json'],
                    '.tmp/index.html': ['index.html']
                }
            },
            dist: {
                files: {
                    '.tmp/scripts/config.js': ['scripts/config.tpl.js'],
                    '.tmp/keycloak.json': ['keycloak.tpl.json'],
                    '<%= paths.dist %>/index.html': ['index.html']
                }
            }
        },
        browserify: {
            develop: {
                files: {
                    '.tmp/scripts/app.js': 'scripts/app.js'
                },
                options: {
                    transform: [['stringify', ['.html']], 'babelify'],
                    watch: true,
                    browserifyOptions: {
                        debug: true
                    }
                }
            },
            dist: {
                files: {
                    '.tmp/scripts/app.js': 'scripts/app.js'
                },
                options: {
                    transform: [['stringify', ['.html']], 'babelify']
                }
            }
        },
        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            dist: {
                files: {
                    '.tmp/scripts/app.js': ['.tmp/scripts/app.js']
                }
            }
        },
        autoprefixer: {
            dist: {
                options: {
                    browsers: ['last 5 versions', 'ie 9']
                },
                expand: true,
                src: '.tmp/**/*.css'
            }
        },
        htmlmin: {
            dist: {
                options: {
                    collapseWhitespace: true
                },
                files: [
                    {
                        expand: true,
                        cwd: '<%= paths.dist %>',
                        src: '*.html',
                        dest: '<%= paths.dist %>'
                    }
                ]
            }
        },
        imagemin: {
            dist: {
                files: [
                    {
                        expand: true,
                        cwd: 'images',
                        src: ['**/*.{png,jpg,gif,svg,jpeg}'],
                        dest: '<%= paths.dist %>/images'
                    }
                ]
            }
        },
        cssmin: {
            options: {
                advanced: false
            }
        },
        uglify: {
            dist: {
                files: [{
                    '<%= paths.dist %>/scripts/app.js': '.tmp/scripts/app.js'
                    },
                    {
                    expand: true,
                    dot: true,
                    cwd: '.tmp/locales',
                    dest: '<%= paths.dist %>/locales',
                    src: [
                        '**/*.js'
                    ]
                }]

            }
        },
        bless: {
            dist: {
                options: {
                    cacheBuster: false
                },
                files: {
                    '<%= paths.dist %>/styles/layout-ie9.css': '.tmp/concat/styles/layout.css'
                }
            }
        },
        copy: {
            dist: {
                files: [
                    {
                        expand: true,
                        dot: true,
                        cwd: '.',
                        dest: '<%= paths.dist %>',
                        src: [
                            '*.{ico,txt}',
                            'favicon.ico'
                        ]
                    },
                    {
                        expand: true,
                        flatten: true,
                        dot: true,
                        cwd: '.',
                        dest: '<%= paths.dist %>',
                        src: [
                            '.tmp/keycloak.json',
                            'favicons/*'
                        ]
                    },
                    {
                        expand: true,
                        flatten: true,
                        dot: true,
                        cwd: '.',
                        dest: '<%= paths.dist %>/fonts',
                        src: [
                            'node_modules/font-awesome/fonts/*.{otf,eot,svg,ttf,woff,wof§f2}'
                        ]
                    },
                    {
                        expand: true,
                        dot: true,
                        cwd: 'locales',
                        dest: '<%= paths.dist %>/locales',
                        src: [
                            '**/*.json'
                        ]
                    }
                ]
            }
        },
        filerev: {
            dist: {
                src: [
                    '<%= paths.dist %>/scripts/**/*.js',
                    '<%= paths.dist %>/styles/**/*.css'
                ]
            }
        },
        usemin: {
            html: ['<%= paths.dist %>/{,*/}*.html'],
            css: ['<%= paths.dist %>/styles/{,*/}*.css'],
            options: {
                dirs: ['<%= paths.dist %>']
            }
        }
    });

    grunt.registerTask('serve', [
        'clean:temp',
        'template:develop',
        'browserify:develop',
        'connect:livereload',
        'watch'
    ]);

    grunt.registerTask('build', [
        'clean',
        'useminPrepare',
        'template:dist',
        'browserify:dist',
        'concat',
        'ngAnnotate',
        'autoprefixer',
        'copy:locale',
        'imagemin',
        'bless',
        'cssmin',
        'uglify',
        'copy:dist',
        'filerev',
        'usemin'
    ]);
};