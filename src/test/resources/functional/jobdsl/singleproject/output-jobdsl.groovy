job('testJob') {
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
            tasks ':clean'
            switches '--no-daemon'
            switches '--no-parallel'
            switches '--no-build-cache'
        }
        gradle {
            useWrapper true
            tasks ':build'
            switches '--no-daemon'
            switches '--no-parallel'
            switches '--no-build-cache'
        }
    }
    publishers {
        checkstyle {
            pattern('build/reports/checkstyle/*.xml')
        }
    }
}

