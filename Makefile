.PHONY: test test-watch

test:
	lein midje

test-watch:
	lein midje :autotest

package:
	lein uberjar

