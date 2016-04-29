export default {
    urls: {
        api: '<%- api %>' || 'http://localhost\:8080/cross-build-rest/v1'
    },
    version: '<%- version %>',
    passErrors: [400, 404, 422, -1]
}