job('first') {
    scm {
        git {
            remote {
                url 'fake://url'
            }
        }
    }
    steps {
        gradle {
            useWrapper true
            tasks ':first:clean'
            switches '--no-daemon'
            switches '--no-parallel'
            switches '--no-build-cache'
        }
        gradle {
            useWrapper true
            tasks ':first:build'
            switches '--no-daemon'
            switches '--no-parallel'
            switches '--no-build-cache'
        }
    }
    publishers {
        checkstyle {
            pattern('first/build/reports/checkstyle/*.xml')
        }
    }
}

job('second') {
    scm {
        git {
            remote {
                url 'fake://url'
            }
        }
    }
    steps {
        gradle {
            useWrapper true
            tasks ':second:clean'
            switches '--no-daemon'
            switches '--no-parallel'
            switches '--no-build-cache'
        }
        gradle {
            useWrapper true
            tasks ':second:build'
            switches '--no-daemon'
            switches '--no-parallel'
            switches '--no-build-cache'
        }
    }
    publishers {
        checkstyle {
            pattern('second/build/reports/checkstyle/*.xml')
        }
    }
}

