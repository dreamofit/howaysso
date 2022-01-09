package cn.ihoway.type;

/**
 * 加密算法类型
 */
public enum AlgorithmType {
    MD5("MD5"),
    MD2("MD2"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    private String algorithm;
    AlgorithmType( String algorithm){
        this.algorithm =algorithm;
    }
    public String getAlgorithm() {
        return algorithm;
    }
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
